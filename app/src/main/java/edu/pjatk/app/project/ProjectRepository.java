package edu.pjatk.app.project;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Repository
public class ProjectRepository {

    private final EntityManager entityManager;

    @Autowired
    public ProjectRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void update(Project project){
        entityManager.merge(project);
    }

    public Optional<Project> getProjectById(Long id) {
        Optional project;
        try {
            project = Optional.of(entityManager.createQuery(
                            "select project from Project project where project.id = :id", Project.class)
                            .setParameter("id", id).getSingleResult());
        }
        catch (NoResultException e){
            project = Optional.empty();
        }
        return project;
    }

    public Optional<List<Project>> getAllNonPrivateProjects(){
        Optional projects;
        try {
            projects = Optional.of(entityManager.createQuery(
                    "SELECT project FROM Project project WHERE project.project_access<>'PRIVATE'")
                            .getResultList()
            );
        } catch (NoResultException e){
            projects = Optional.empty();
        }
        return projects;
    }

    public List<Long> randomUnicalId(Integer numberOfProjects, List<Long> projectIds, List<Long> blacklist){
        List<Long> uniqueIdList = new ArrayList<>();
        Random random = new Random();
        if (projectIds.size() <= numberOfProjects) {
            uniqueIdList = projectIds;
            return uniqueIdList;
        }
        else {
            for (int i = 0; i < numberOfProjects; i++) {
                int chosenProjectId = random.nextInt(0, projectIds.size() - 1);
                long randomChosenId = projectIds.get(chosenProjectId);
                while (blacklist.contains(randomChosenId))
                {
                    chosenProjectId = random.nextInt(0, projectIds.size() - 1);
                    randomChosenId = projectIds.get(chosenProjectId);
                }
                uniqueIdList.add(randomChosenId);
                blacklist.add(randomChosenId);
            }
            return uniqueIdList;
        }
    }

    public Optional<List<Project>> getRandomRecommendedProjects(int projectQuantity){
        List<Long> blacklist = new ArrayList<>();
        List<Long> projectIds = entityManager.createQuery(
                "SELECT project.id FROM Project project WHERE project.project_access<>'PRIVATE'", Long.class
                ).getResultList();

        Optional projects;
        List<Long> selectedProjects = randomUnicalId(projectQuantity, projectIds, blacklist);
        try {
            projects = Optional.of(entityManager.createQuery(
                            "SELECT project FROM Project project WHERE project.id IN (:selectedProjects)", Project.class)
                            .setParameter("selectedProjects", selectedProjects).getResultList());
        } catch (NoResultException e){
            projects = Optional.empty();
        }
        return projects;
    }

    public Optional<List<Project>> getAllProposedProjects(Long id){
        Optional<List<Project>> projects;
        try {
            projects =  Optional.of(entityManager.createQuery(
                            "select project from Project project where project.project_access<>'PRIVATE' and project.creator.id<>:id", Project.class)
                    .setParameter("id", id).getResultList());
        }
        catch (NoResultException noResultException){
            projects = Optional.empty();
        }
        return projects;
    }

    public Optional<List<Project>> getAllFromLastMonth(){
        Optional<List<Project>> projects;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        LocalDateTime date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        try {
            projects =  Optional.of(entityManager.createQuery(
                            "SELECT project FROM Project project WHERE project.project_access='PUBLIC' and project.creation_date >= :date", Project.class)
                    .setParameter("date", date).getResultList());
        }
        catch (NoResultException noResultException){
            projects = Optional.empty();
        }
        return projects;
    }

    public Optional<List<Project>> getProjectsByTitle(String project_name) {
        Optional<List<Project>> projects;
        try {
            projects =  Optional.of(entityManager.createQuery(
                            "select project from Project project where project.project_name like concat('%',:project_name,'%') and project.project_access <> 'PRIVATE'", Project.class)
                    .setParameter("project_name", project_name+"%").getResultList());
        }
        catch (NoResultException noResultException){
            projects = Optional.empty();
        }
        return projects;
    }
    
    public Optional<List<Project>> getByCategory(String categoryTitle) {
        Optional<List<Project>> projects;
        try {
            projects =  Optional.of(entityManager.createQuery(
                            "select project from Project project join project.categories c where c.title = :title and project.project_access <> 'PRIVATE'", Project.class)
                    .setParameter("title", categoryTitle).getResultList());
        }
        catch (NoResultException noResultException){
            projects = Optional.empty();
        }
        return projects;
    }

    public Optional<List<Project>> getAllCreatorProjects(Long creator_id) {
        Optional<List<Project>> project;
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

    public Optional<List<Project>> getProjectsByTitleWithPagination(String projectTitle, int pageNumber, int pageSize){
        Optional<List<Project>> projects;
        try {
            Query query = entityManager.createQuery("select project from Project project where project.project_name like concat('%',:project_name,'%') and project.project_access <> 'PRIVATE'");
            query.setParameter("project_name", projectTitle+"%");
            query.setFirstResult((pageNumber-1) * pageSize);
            query.setMaxResults(pageSize);
            projects = Optional.of(query.getResultList());
        }
        catch (NoResultException noResultException){
            projects = Optional.empty();
        }
        return projects;
    }

    public Optional<Long> getProjectsNumberByTitle(String projectTitle) {
        Optional<Long> projectsNumber;
        try {
            projectsNumber =  Optional.of(entityManager.createQuery(
                            "select count(project.id) from Project project where project.project_name like concat('%',:project_name,'%') and project.project_access <> 'PRIVATE'", Long.class)
                    .setParameter("project_name", projectTitle+"%").getSingleResult());
        }
        catch (NoResultException noResultException){
            projectsNumber = Optional.empty();
        }
        return projectsNumber;
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

}
