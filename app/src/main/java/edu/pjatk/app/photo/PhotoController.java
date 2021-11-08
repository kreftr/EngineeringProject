package edu.pjatk.app.photo;

import edu.pjatk.app.response.ResponseMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;


@RestController
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

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
            if (photoService.uploadPhoto(file)) {
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

        Optional<Photo> photo = photoService.findPhotoByUrl("uploads/photo/"+filename);

        if (photo.isEmpty()){
            return new ResponseEntity(
                    new ResponseMessage("Photo not found"), HttpStatus.NOT_FOUND
            );
        }
        else {
            String pathToUploadFolder = "uploads/photo/";
            File file = new File(pathToUploadFolder+filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            InputStream in = new FileInputStream(file);
            byte[] media = IOUtils.toByteArray(in);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            return new ResponseEntity(
                    media, headers, HttpStatus.OK
            );
        }
}

}
