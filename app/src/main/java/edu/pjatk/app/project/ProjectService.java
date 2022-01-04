package edu.pjatk.app.project;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.project.category.CategoryService;
import edu.pjatk.app.request.ProjectRequest;
import edu.pjatk.app.response.project.FullProjectResponse;
import edu.pjatk.app.response.project.MiniProjectResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRepository;
import edu.pjatk.app.user.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PhotoService photoService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CategoryService categoryService,
                          UserService userService, PhotoService photoService) {
        this.projectRepository = projectRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.photoService = photoService;
    }


    public Optional<FullProjectResponse> getProjectById(Long id) {

        Optional<Project> projectOptional = projectRepository.getProjectById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projectOptional.isPresent()){
            Project project = projectOptional.get();
            String projectPhoto, authorPhoto, ytLink, gitLink, fbLink, kickLink;
            Set<String> categories = new HashSet<>();

            try { projectPhoto = project.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
            try { authorPhoto = project.getCreator().getProfile().getPhoto().getFileName(); }
            catch (NullPointerException e) { authorPhoto = null;}
            try { ytLink = project.getYoutube_link(); } catch (NullPointerException e) { ytLink = null;}
            try { gitLink = project.getGithub_link(); } catch (NullPointerException e) { gitLink = null;}
            try { fbLink = project.getFacebook_link(); } catch (NullPointerException e) { fbLink = null;}
            try { kickLink = project.getKickstarter_link(); } catch (NullPointerException e) { kickLink = null;}

            for (Category c : project.getCategories()){
                categories.add(c.getTitle());
            }

            return Optional.of(new FullProjectResponse(
                    project.getId(), projectPhoto,
                    project.getProject_name(), project.getProject_introduction(),
                    project.getProject_description(), project.getCreation_date().format(formatter),
                    project.getProject_status().name(), project.getProject_access().name(), categories,
                    ytLink, gitLink, fbLink, kickLink,
                    project.getCreator().getId(), project.getCreator().getUsername(),
                    authorPhoto
            ));
        }
        else return Optional.empty();
    }

    public Set<MiniProjectResponse> getProjectByName(String project_name) {

        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projectList = projectRepository.getProjectByName(project_name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projectList.isPresent() && !projectList.get().isEmpty()){

            String projectPhoto, authorPhoto;

            for (Project p : projectList.get()){

                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                p.getCreator().getUsername(), authorPhoto
                        )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    public Set<MiniProjectResponse> getAllProjects(Long creator_id) {

        Set<MiniProjectResponse> projectResponses = new HashSet<>();
        Optional<List<Project>> projects = projectRepository.getAllProjects(creator_id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (projects.isPresent() && !projects.get().isEmpty()){
            String projectPhoto, authorPhoto;

            for (Project p : projects.get()){
                Set<String> categories = new HashSet<>();
                try { projectPhoto = p.getPhoto().getFileName(); } catch (NullPointerException e) { projectPhoto = null;}
                try { authorPhoto = p.getCreator().getProfile().getPhoto().getFileName(); }
                catch (NullPointerException e) { authorPhoto = null;}

                for (Category c : p.getCategories()){
                    categories.add(c.getTitle());
                }

                projectResponses.add(
                        new MiniProjectResponse(
                                p.getId(), projectPhoto, p.getProject_name(), p.getProject_introduction(),
                                categories, p.getCreation_date().format(formatter), p.getCreator().getId(),
                                p.getCreator().getUsername(), authorPhoto
                                )
                );
            }
            return projectResponses;
        }
        else return Collections.emptySet();
    }

    @Transactional
    public void createProject(ProjectRequest projectRequest, MultipartFile photo){

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Photo projectPhoto = (photo != null ? photoService.uploadPhoto(photo) : null);

        if (loggedUser.isPresent())
        {
            Set<Category> categories = new HashSet<>();
            for (String category : projectRequest.getCategory()){
                categories.add(categoryService.getCategoryByTitle(category).get());
            }

            Project project = new Project(
                    projectRequest.getTitle(), projectRequest.getIntroduction(), projectRequest.getDescription(),
                    LocalDateTime.now(), categories, ProjectStatus.OPEN, projectRequest.getAccess(),
                    projectRequest.getYoutubeLink(), projectRequest.getFacebookLink(), projectRequest.getGithubLink(),
                    projectRequest.getKickstarterLink(), loggedUser.get(), projectPhoto
            );

            projectRepository.createProject(project);
        }
    }

    public void deleteProject(Long id) {
        projectRepository.deleteProject(id);
    }

    public void editProjectName(Long id, String project_name) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectName(id, project_name);
        }
    }

    public void editProjectCategory(Long id, String project_category) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectCategory(id, project_category);
        }
    }

    public void editProjectStatus(Long id, String project_status) {
        Optional<Project> project = projectRepository.getProjectById(id);
        if (project.isPresent()){
            projectRepository.editProjectStatus(id, project_status);
        }
    }
}
