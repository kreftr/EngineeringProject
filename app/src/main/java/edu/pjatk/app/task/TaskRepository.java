package edu.pjatk.app.task;

import edu.pjatk.app.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

    @Transactional
    public void createTask(Task task){
        entityManager.persist(task);
    }
}
