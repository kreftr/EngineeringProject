package edu.pjatk.app.task;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    @GetMapping(value = "/getTaskById/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
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

    @GetMapping(value = "/getTaskByName/{task_name}")
    public ResponseEntity<?> getTaskByName(@PathVariable String task_name) {
        Optional<Task> task = taskService.getTaskByName(task_name);
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

    @PostMapping(value = "/createTask/{task_name}/}/{task_status}/{expiration_date}/{task_description}")
    public ResponseEntity<?> createProject(@PathVariable String task_name,
                                           @PathVariable String task_description,
                                           @PathVariable String task_status,
                                           @PathVariable LocalDateTime expiration_date) {
        if (task_name.length() > 0 && task_status.length() > 0) {
            taskService.createTask(task_name, task_description, task_status, expiration_date);
            return new ResponseEntity<>(
                    new ResponseMessage("Task created!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Error wrong attributes for Task creation"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping(value = "/createTask/{task_name}/{task_status}/{expiration_date}")
    public ResponseEntity<?> createProject(@PathVariable String task_name,
                                           @PathVariable String task_status,
                                           @PathVariable LocalDateTime expiration_date) {
        if (task_name.length() > 0 && task_status.length() > 0) {
            taskService.createTask(task_name, task_status, expiration_date);
            return new ResponseEntity<>(
                    new ResponseMessage("Task created!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Error wrong attributes for Task creation"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/deleteTask/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(
                new ResponseMessage("Task deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/editTaskName/{id}/{task_name}")
    public ResponseEntity<?> editTaskName(@PathVariable long id, @PathVariable String task_name) {
        taskService.editTaskName(id, task_name);
        return new ResponseEntity<>(
                new ResponseMessage("Task name change is complete"), HttpStatus.OK
        );
    }
}
