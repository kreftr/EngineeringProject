package edu.pjatk.app.project.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private EntityManager entityManager;

    @Autowired
    public TaskRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    void add(Task task){
        entityManager.persist(task);
    }

    void update(Task task) {
        entityManager.merge(task);
    }

    void remove(Task task) {
        entityManager.remove(task);
    }

    public Optional<List<Task>> getAllByProjectId(Long projectId) {
        Optional tasks;
        try {
            tasks = Optional.of(
                    entityManager.createQuery("SELECT task FROM Task task WHERE task.project.id=:id", Task.class).
                            setParameter("id", projectId).getResultList()
            );
        } catch (NoResultException e) {
            tasks = Optional.empty();
        }
        return tasks;
    }

    public Optional<Task> getById(Long taskId){
        Optional tasks;
        try {
            tasks = Optional.of(
                    entityManager.createQuery("SELECT task FROM Task task WHERE task.id=:id", Task.class).
                            setParameter("id", taskId).getSingleResult()
            );
        } catch (NoResultException e) {
            tasks = Optional.empty();
        }
        return tasks;
    }

}
