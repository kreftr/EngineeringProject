package edu.pjatk.app.project;


import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping(value = "/createProject/{id}/{project_name}/{creation_date}/{project_category}/{project_status}/" +
            "{project_creator}")
    public ResponseEntity<?> createProject(@PathVariable Long id, @PathVariable String project_name,
                                           @PathVariable LocalDateTime creation_date, @PathVariable String project_category,
                                           @PathVariable String project_status, @PathVariable Long project_creator) {
//        TODO finish this function
        return null;
    }

}
