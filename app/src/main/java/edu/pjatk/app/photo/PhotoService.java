package edu.pjatk.app.photo;

import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
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
    public Photo uploadPhoto(MultipartFile file){

        try {
            byte[] data = file.getBytes();
            String randomName = UUID.randomUUID()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(pathToPhotos + randomName);
            Files.write(path, data);
            Photo photo = new Photo(randomName);
            photoRepository.save(photo);
            return photo;
        } catch (Exception e){
            return null;
        }

    }

    @Transactional
    public void removePhoto(Photo photo){
        try {
            photoRepository.remove(photo);
            File file = new File(pathToPhotos+photo.getFileName());
            file.delete();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public boolean isPhoto(MultipartFile file){
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
