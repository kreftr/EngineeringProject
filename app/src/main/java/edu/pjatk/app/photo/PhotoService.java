package edu.pjatk.app.photo;

import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Value("${uploads.photo.path}")
    private String pathToPhotos;

    @Autowired
    public PhotoService(PhotoRepository photoRepository){
        this.photoRepository = photoRepository;
    }

    @Transactional
    public boolean uploadPhoto(MultipartFile file){

        try {
            byte[] data = file.getBytes();
            //TODO: Find a way to associate the photo with the user in case of database corruption
            String randomName = UUID.randomUUID()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(pathToPhotos + randomName);
            Files.write(path, data);
            photoRepository.save(new Photo(randomName));
            return true;
        } catch (Exception e){
            return false;
        }

    }

    public boolean isPhoto(MultipartFile file){
        //TODO: Find a better way to check if uploaded file is an image
        if (file.getContentType().split("/")[0].equals("image")) return true;
        else return false;
    }

    public Optional<Photo> findPhotoById(Long id){
        return photoRepository.findById(id);
    }

    public Optional<Photo> findPhotoByFileName(String fileName) {
        return photoRepository.findByFileName(fileName);
    }

}
