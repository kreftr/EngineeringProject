package edu.pjatk.app.task;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/task")

public class TaskControler {
    private final TaskService taskService;

    @Autowired
    private TaskControler(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/findTaskById/{id}")
    public ResponseEntity<?> findTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.findTaskById(id);
        if (task.isPresent())
        {
            return new ResponseEntity<>(
                    task, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no tasks!"), HttpStatus.NOT_FOUND
            );
        }
    }
}
