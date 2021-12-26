package edu.pjatk.app.file;

import edu.pjatk.app.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Optional<File> getFileById(Long id) {
        return fileRepository.getFileById(id);
    }

    public void createFile(String file_name, Long file_size, String file_url){
        LocalDateTime date = LocalDateTime.now();
        File file = new File(file_name, file_size, file_url);
        fileRepository.createFile(file);
    }

    public void deleteFile(Long id) {
        fileRepository.deleteFile(id);
    }

    public void editFileName(Long id, String file_name) {
        Optional<File> file = fileRepository.getFileById(id);
        if (file.isPresent()){
            fileRepository.editFileName(id, file_name);
        }
    }
}
