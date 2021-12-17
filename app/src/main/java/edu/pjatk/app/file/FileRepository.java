package edu.pjatk.app.file;

import edu.pjatk.app.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class FileRepository {
    private final EntityManager entityManager;

    @Autowired
    public FileRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<File> getFileById(Long id) {
        return Optional.of(entityManager.find(File.class, id));
    }

    @Transactional
    public void createFile(File file){
        entityManager.persist(file);
    }

    @Transactional
    public void deleteFile(Long id){
        File file = entityManager.find(File.class, id);
        entityManager.remove(file);
    }

    @Transactional
    public void editFileName(Long id, String file_name) {
        File file = entityManager.find(File.class, id);
        file.setFile_name(file_name);
    }
}
