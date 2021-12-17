package edu.pjatk.app.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
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

    public Optional<Project> findProjectByName(String project_name) {
        Optional<Project> project;
        try {
            project =  Optional.of(entityManager.createQuery(
                            "select project from Project project where project.project_name = :project_name", Project.class)
                    .setParameter("project_name", project_name).getSingleResult());
        }
        catch (NoResultException noResultException){
            project = Optional.empty();
        }
        return project;
    }

    public Optional<List<Project>> getAllProjects(Long project_creator) {
        Optional<List<Project>> project;
        try {
            project =  Optional.of(entityManager.createQuery(
                "select project from Project project where project.creator = :project_creator", Project.class)
                .setParameter("project_creator", project_creator).getResultList());
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
    public void editProjectName(Project project) {
        entityManager.merge(project);
    }


}
