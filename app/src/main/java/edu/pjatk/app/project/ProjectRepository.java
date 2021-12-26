package edu.pjatk.app.project;


import edu.pjatk.app.user.User;
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
        Optional project;
        try {
            project =  Optional.of(entityManager.createQuery(
                            "select project from Project project where project.project_name = :project_name", Project.class)
                    .setParameter("project_name", project_name).getResultList());
        }
        catch (NoResultException noResultException){
            project = Optional.empty();
        }
        return project;
    }

    public Optional<Project> getAllProjects(Long creator_id) {
        Optional project;
        try {
            project =  Optional.of(entityManager.createQuery(
                "select project from Project project where project.creator.id = :creator_id", Project.class)
                .setParameter("creator_id", creator_id).getResultList());
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
        entityManager.persist(project);
    }

    @Transactional
    public void editProjectCategory(Long id, String project_category) {
        Project project = entityManager.find(Project.class, id);
        project.setProject_name(project_category);
        entityManager.persist(project);
    }

    @Transactional
    public void editProjectStatus(Long id, String project_status) {
        Project project = entityManager.find(Project.class, id);
        project.setProject_name(project_status);
        entityManager.persist(project);
    }


}
