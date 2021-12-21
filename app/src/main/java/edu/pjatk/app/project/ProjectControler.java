package edu.pjatk.app.project;


import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/project")

public class ProjectControler {
    private final ProjectService projectService;

    @Autowired
    private ProjectControler(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping(value = "/getProjectById/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent())
        {
            return new ResponseEntity<>(
                    project, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getProjectByName/{project_name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String project_name) {
        Optional<Project> project = projectService.getProjectByName(project_name);
        if (project.isPresent())
        {
            return new ResponseEntity<>(
                    project, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getAllProjects/{project_creator}")
    public ResponseEntity<?> getAllProjects(@PathVariable Long project_creator) {
        Optional<Project> project = projectService.getAllProjects(project_creator);
        if (project.isPresent())
        {
            return new ResponseEntity<>(
                    project, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/createProject/{project_name}/{project_category}/{project_status}/" +
            "{project_creator}")
    public ResponseEntity<?> createProject(@PathVariable String project_name,
                                           @PathVariable String project_category,
                                           @PathVariable String project_status,
                                           @PathVariable Long project_creator) {
        if (project_name.length() > 0 && project_category.length() > 0 && project_status.length() > 0) {
            projectService.createProject(project_name, project_category, project_status, project_creator);
            return new ResponseEntity<>(
                    new ResponseMessage("Project created!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Error wrong attributes for project creation"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping(value = "/createProject/{project_name}/{project_category}/{project_status}/" +
            "{project_creator}/{project_description}")
    public ResponseEntity<?> createProject(@PathVariable String project_name,
                                           @PathVariable String project_description,
                                           @PathVariable String project_category,
                                           @PathVariable String project_status,
                                           @PathVariable Long project_creator) {
        if (project_name.length() > 0 && project_category.length() > 0 && project_status.length() > 0) {
            projectService.createProject(project_name, project_description, project_category, project_status, project_creator);
            return new ResponseEntity<>(
                    new ResponseMessage("Project created!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Error wrong attributes for project creation"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/deleteProject/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(
                new ResponseMessage("Project deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectName/{id}/{project_name}")
    public ResponseEntity<?> editProjectName(@PathVariable long id, @PathVariable String project_name) {
        projectService.editProjectName(id, project_name);
        return new ResponseEntity<>(
                new ResponseMessage("Project name change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectCategory/{id}/{project_category}")
    public ResponseEntity<?> editProjectCategory(@PathVariable long id, @PathVariable String project_category) {
        projectService.editProjectCategory(id, project_category);
        return new ResponseEntity<>(
                new ResponseMessage("Project category change is complete"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editProjectStatus/{id}/{project_status}")
    public ResponseEntity<?> editProjectStatus(@PathVariable long id, @PathVariable String project_status) {
        projectService.editProjectStatus(id, project_status);
        return new ResponseEntity<>(
                new ResponseMessage("Project status change is complete"), HttpStatus.OK
        );
    }
}
