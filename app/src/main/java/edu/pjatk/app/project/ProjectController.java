package edu.pjatk.app.project;


import edu.pjatk.app.request.ProjectRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.response.project.FullProjectResponse;
import edu.pjatk.app.response.project.MiniProjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PostUpdate;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/project")

public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    private ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping(value = "/getProjectById/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {

        Optional<FullProjectResponse> projectResponse = projectService.getProjectById(id);

        if (projectResponse.isPresent())
        {
            return new ResponseEntity<>(
                    projectResponse.get(), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getProjectByName/{project_name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String project_name) {

        Set<MiniProjectResponse> projects = projectService.getProjectByName(project_name);

        if (projects.isEmpty())
        {
            return new ResponseEntity<>(
                    new ResponseMessage("No matches for '"+project_name+"'"), HttpStatus.NOT_FOUND
            );
        }
        else {
            return new ResponseEntity<>(
                    projects, HttpStatus.OK
            );
        }
    }

    @GetMapping(value = "/getAllProjects/{creator_id}")
    public ResponseEntity<?> getAllProjects(@PathVariable Long creator_id) {
        Set<MiniProjectResponse> projects = projectService.getAllProjects(creator_id);

        if (!projects.isEmpty())
        {
            return new ResponseEntity<>(
                    projects, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/createProject", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createProject(@Valid @RequestPart ProjectRequest projectRequest,
                                           @RequestPart(required = false) MultipartFile projectPhoto) {

        projectService.createProject(projectRequest, projectPhoto);
        return new ResponseEntity<>(new ResponseMessage("Project created!"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteProject/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(
                new ResponseMessage("Project deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectName/{id}/{project_name}")
    public ResponseEntity<?> editProjectName(@PathVariable long id, @PathVariable String project_name) {
        projectService.editProjectName(id, project_name);
        return new ResponseEntity<>(
                new ResponseMessage("Project name change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectCategory/{id}/{project_category}")
    public ResponseEntity<?> editProjectCategory(@PathVariable long id, @PathVariable String project_category) {
        projectService.editProjectCategory(id, project_category);
        return new ResponseEntity<>(
                new ResponseMessage("Project category change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectStatus/{id}/{project_status}")
    public ResponseEntity<?> editProjectStatus(@PathVariable long id, @PathVariable String project_status) {
        projectService.editProjectStatus(id, project_status);
        return new ResponseEntity<>(
                new ResponseMessage("Project status change is complete"), HttpStatus.OK
        );
    }

}
