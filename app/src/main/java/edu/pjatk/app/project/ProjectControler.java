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

    @GetMapping(value = "/findProjectById/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.findProjectById(id);
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
}
