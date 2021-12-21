package edu.pjatk.app.file;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/file")

public class FileControler {
    private final FileService fileService;

    @Autowired
    private FileControler(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping(value = "/getFileById/{id}")
    public ResponseEntity<?> getFileById(@PathVariable Long id) {
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent())
        {
            return new ResponseEntity<>(
                    file, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no files!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/createFile/{file_name}/{file_size}/{file_url}")
    public ResponseEntity<?> createFile(@PathVariable String file_name,
                                           @PathVariable Long file_size,
                                           @PathVariable String file_url) {
        if (file_name.length() > 0 && file_size > 0) {
            fileService.createFile(file_name, file_size, file_url);
            return new ResponseEntity<>(
                    new ResponseMessage("File created!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Error wrong attributes for File creation"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/deleteFile/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(
                new ResponseMessage("File deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editFileName/{id}/{file_name}")
    public ResponseEntity<?> editFileName(@PathVariable long id, @PathVariable String file_name) {
        fileService.editFileName(id, file_name);
        return new ResponseEntity<>(
                new ResponseMessage("File name change is complete"), HttpStatus.OK
        );
    }

}
