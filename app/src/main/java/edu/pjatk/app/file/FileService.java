package edu.pjatk.app.file;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.user.User;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class FileService {

    private final FileRepository fileRepository;

    @Value("${uploads.files.path}")
    private String pathToFiles;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public Optional<File> findFileById(Long id){
        return fileRepository.findById(id);
    }

    @Transactional
    public File uploadFile(MultipartFile file, String fullPath, Project project, User user){
        try {
            byte[] data = file.getBytes();
            String randomName = UUID.randomUUID()+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(pathToFiles + randomName);
            Files.write(path, data);
            File fileToUpload = new File(fullPath, randomName,
                    Files.size(path), LocalDateTime.now(), project, user);
            fileRepository.save(fileToUpload);
            return fileToUpload;
        } catch (Exception e){
            return null;
        }
    }

    @Transactional
    public void removeFile(File file){
        try {
            fileRepository.remove(file);
            java.io.File f = new java.io.File(pathToFiles+file.getUrl());
            f.delete();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
