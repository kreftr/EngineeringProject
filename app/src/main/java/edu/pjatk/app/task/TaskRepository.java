package edu.pjatk.app.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class TaskRepository {
    private final EntityManager entityManager;

    @Autowired
    public TaskRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Task> findTaskById(Long id) {
        return Optional.of(entityManager.find(Task.class, id));
    }
}
