package edu.pjatk.app.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Optional<File> findFileById(Long id) {
        return fileRepository.findFileById(id);
    }
}
