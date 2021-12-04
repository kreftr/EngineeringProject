package edu.pjatk.app.photo;

import edu.pjatk.app.response.ResponseMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;


@RestController
@RequestMapping("/photo")
@CrossOrigin("http://localhost:3000")
public class PhotoController {

    private final PhotoService photoService;

    @Value("${uploads.photo.path}")
    private String pathToPhotos;

    @Autowired
    public PhotoController(PhotoService photoService){
        this.photoService = photoService;
    }


    @PostMapping
    public ResponseEntity uploadPhoto(@RequestPart MultipartFile file) {

        if (file.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("File is empty!"), HttpStatus.BAD_REQUEST
            );
        } else if (!photoService.isPhoto(file)){
            return new ResponseEntity(
                    new ResponseMessage("Uploaded file is not a photo!"), HttpStatus.BAD_REQUEST
            );
        }
        else {
            if (photoService.uploadPhoto(file) != null) {
                return new ResponseEntity(
                        new ResponseMessage("Photo uploaded!"), HttpStatus.OK
                );
            }
            else{
                return new ResponseEntity(
                        new ResponseMessage("Serverside error!"), HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }

    @GetMapping
    public ResponseEntity<byte[]> getPhoto(@RequestParam String filename) throws IOException {

        Optional<Photo> photo = photoService.findPhotoByFileName(filename);

        if (photo.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Photo not found"), HttpStatus.NOT_FOUND
            );
        }
        else {
            File file = new File(pathToPhotos+filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(Files.probeContentType(file.toPath())));
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            InputStream in = new FileInputStream(file);
            byte[] bytesToReturn = IOUtils.toByteArray(in);
            in.close();
            return new ResponseEntity(
                    bytesToReturn , headers, HttpStatus.OK
            );
        }
    }

}
