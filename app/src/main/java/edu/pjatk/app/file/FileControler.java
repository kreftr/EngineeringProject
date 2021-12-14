package edu.pjatk.app.file;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/findFileById/{id}")
    public ResponseEntity<?> findFileById(@PathVariable Long id) {
        Optional<File> file = fileService.findFileById(id);
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

}
