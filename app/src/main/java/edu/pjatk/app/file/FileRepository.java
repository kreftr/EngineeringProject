package edu.pjatk.app.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class FileRepository {
    private final EntityManager entityManager;

    @Autowired
    public FileRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<File> findFileById(Long id) {
        return Optional.of(entityManager.find(File.class, id));
    }
}
