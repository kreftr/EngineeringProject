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

    public Optional<Task> getTaskById(Long id) {
        return Optional.of(entityManager.find(Task.class, id));
    }

    public Optional<Task> getTaskByName(String task_name) {
        return Optional.of(entityManager.find(Task.class, task_name));
    }

    @Transactional
    public void createTask(Task task){
        entityManager.persist(task);
    }

    @Transactional
    public void deleteTask(Long id){
        Task task = entityManager.find(Task.class, id);
        entityManager.remove(task);
    }

    @Transactional
    public void editTaskName(Long id, String task_name) {
        Task task = entityManager.find(Task.class, id);
        task.setTask_name(task_name);
    }

}
