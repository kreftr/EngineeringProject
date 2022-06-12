package edu.pjatk.app.file;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.participant.ParticipantRole;
import edu.pjatk.app.project.participant.ParticipantService;
import edu.pjatk.app.request.ContentRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final ParticipantService participantService;
    private final ProjectRepository projectRepository;

    @Value("${uploads.files.path}")
    private String pathToFiles;

    @Autowired
    private FileController(FileService fileService, UserService userService, ProjectRepository projectRepository,
                           ParticipantService participantService){
        this.fileService = fileService;
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.participantService = participantService;
    }


    @PostMapping(value = "/upload", params = "projectId")
    public ResponseEntity uploadFile(@RequestPart MultipartFile file, @RequestPart String fullPath, @RequestParam Long projectId) {

        Optional<Project> project = projectRepository.getProjectById(projectId);

        if (project.isPresent()){
            Optional<User> loggedUser = userService.findUserByUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );

            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), project.get().getId()
            );


            if (participant.isPresent()) {

                if (file.isEmpty()){
                    return new ResponseEntity(
                            new ResponseMessage("File is empty!"), HttpStatus.BAD_REQUEST
                    );
                }
                else {
                    if (fileService.uploadFile(file, fullPath, project.get(), loggedUser.get())!=null){
                        return new ResponseEntity(
                                new ResponseMessage("File uploaded!"), HttpStatus.OK
                        );
                    }
                    else{
                        return new ResponseEntity(
                                new ResponseMessage("Couldn't upload file"), HttpStatus.INTERNAL_SERVER_ERROR
                        );
                    }
                }

            }
            else {
                return new ResponseEntity(
                        new ResponseMessage("You must join to project to upload files"), HttpStatus.UNAUTHORIZED
                );
            }
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("Project with this id does not exist"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/download", params = "fileId")
    public ResponseEntity<Resource> getFile(@RequestParam Long fileId) throws IOException {

        Optional<File> file = fileService.findFileById(fileId);

        if (file.isPresent()){

            Optional<User> loggedUser = userService.findUserByUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );
            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), file.get().getProject().getId()
            );


            if (participant.isPresent()) {
                java.io.File f = new java.io.File(pathToFiles+file.get().getUrl());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", f.getName()));
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");

                InputStreamResource resource = new InputStreamResource(new FileInputStream(f));

                return ResponseEntity.ok().headers(headers).contentLength(f.length())
                        .contentType(MediaType.parseMediaType("application/txt")).body(resource);
            }
            else {
                return new ResponseEntity(
                        new ResponseMessage("You must join to project to download this file"), HttpStatus.UNAUTHORIZED
                );
            }
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("This file does not exist"), HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping(value = "/remove", params = "fileId")
    public ResponseEntity removeFile(@RequestParam Long fileId){

        Optional<File> file = fileService.findFileById(fileId);

        if (file.isPresent()){

            Optional<User> loggedUser = userService.findUserByUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );
            Optional<Participant> participant = participantService.getParticipantByUserAndProject(
                    loggedUser.get().getId(), file.get().getProject().getId()
            );

            if (participant.isEmpty()){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            else if (loggedUser.get().equals(file.get().getUser()) ||
                    participant.get().getParticipantRole().equals(ParticipantRole.MODERATOR) ||
                    participant.get().getParticipantRole().equals(ParticipantRole.OWNER)
            ) {
                fileService.removeFile(file.get());
                return new ResponseEntity(HttpStatus.OK);
            }
            else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("This file does not exist"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/toggleLock/{fileId}/{isLocked}")
    public ResponseEntity toggleFileLock(@PathVariable Long fileId, @PathVariable Boolean isLocked){
        Optional<File> fileOptional = fileService.findFileById(fileId);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            fileService.toggleFileLock(file, isLocked);
            return new ResponseEntity(
                    new ResponseMessage("File status changed"), HttpStatus.OK
            );
        } else {
            return new ResponseEntity(
                    new ResponseMessage("File does not exist"), HttpStatus.NOT_FOUND
            );
        }

    }

    @GetMapping(value = "/isLocked")
    public ResponseEntity isLocked(@RequestParam Long fileId) {
        Optional<File> file = fileService.findFileById(fileId);
        if (file.isPresent()) {
            return new ResponseEntity(file.get().getIsLocked(), HttpStatus.OK);
        }
        else  {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/edit", params = "fileId")
    public ResponseEntity editTextFile(@RequestParam Long fileId, @RequestBody ContentRequest request) throws IOException {
        Optional<File> file = fileService.findFileById(fileId);
        if (file.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else {
            Optional<Participant> participant = participantService.getParticipantByUsernameAndProjectId(
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    file.get().getProject().getId()
            );
            if (participant.isEmpty()) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            else {
                fileService.editTextFile(fileId, request.getContent());
                return new ResponseEntity(HttpStatus.OK);
            }
        }
    }

}
