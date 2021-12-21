package edu.pjatk.app.project;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class ProjectRepository {
    private final EntityManager entityManager;

    @Autowired
    public ProjectRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Project> getProjectById(Long id) {
        return Optional.of(entityManager.find(Project.class, id));
    }

    public Optional<Project> getProjectByName(String project_name) {
        return Optional.of(entityManager.find(Project.class, project_name));
    }

    public Optional<Project> getAllProjects(Long project_creator) {
        Optional project;
        try {
            project =  Optional.of(entityManager.createQuery(
                "select project from Project project where project.creator = :project_creator", Project.class)
                .setParameter("project_creator", project_creator));
        }
        catch (NoResultException noResultException){
            project = Optional.empty();
        }
        return project;
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
    public void editProjectName(Long id, String project_name) {
        Project project = entityManager.find(Project.class, id);
        project.setProject_name(project_name);
    }

    @Transactional
    public void editProjectCategory(Long id, String project_category) {
        Project project = entityManager.find(Project.class, id);
        project.setProject_name(project_category);
    }

    @Transactional
    public void editProjectStatus(Long id, String project_status) {
        Project project = entityManager.find(Project.class, id);
        project.setProject_name(project_status);
    }


}
