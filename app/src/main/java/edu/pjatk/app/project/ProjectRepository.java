package edu.pjatk.app.project;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class ProjectRepository {
    private final EntityManager entityManager;

    @Autowired
    public ProjectRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Project> findProjectById(Long id) {
        return Optional.of(entityManager.find(Project.class, id));
    }

    @Transactional
    public void createProject(Project project){
        entityManager.persist(project);
    }

    @Transactional
    public void deleteProject(Long id){
        Project project = entityManager.find(Project.class, id);
        entityManager.remove(project);
    }

    @Transactional
    public void editProjectName(Long id) {
        Project project = entityManager.find(Project.class, id);
        entityManager.merge(project);
    }


}
