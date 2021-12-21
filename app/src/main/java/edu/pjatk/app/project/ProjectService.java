package edu.pjatk.app.project;

import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.getProjectById(id);
    }

    public Optional<Project> getProjectByName(String project_name) {
        return projectRepository.getProjectByName(project_name);
    }

    public Optional<Project> getAllProjects(Long project_creator) {
        return projectRepository.getAllProjects(project_creator);
    }

    public void createProject(String project_name, String project_category, String project_status, Long project_creator){
        LocalDateTime date = LocalDateTime.now();
        Optional<User> author = userRepository.findById(project_creator);
        if (author.isPresent())
        {
            Project project = new Project(project_name, date, project_category, project_status, author.get());
            projectRepository.createProject(project);
        }
    }

    public void createProject(String project_name, String project_description,String project_category, String project_status, Long project_creator){
        LocalDateTime date = LocalDateTime.now();
        Optional<User> author = userRepository.findById(project_creator);
        if (author.isPresent())
        {
            Project project = new Project(project_name, project_description, date, project_category, project_status, author.get());
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
