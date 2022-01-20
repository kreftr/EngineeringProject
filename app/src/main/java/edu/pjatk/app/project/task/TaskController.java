package edu.pjatk.app.project.task;

import edu.pjatk.app.request.TaskRequest;
import edu.pjatk.app.response.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }


    @GetMapping(value = "/getAllProjectTasks/{projectId}")
    public ResponseEntity getAllProjectTasks(@PathVariable Long projectId){
        Set<TaskResponse> tasks = taskService.getAllProjectTasks(projectId);
        if (!tasks.isEmpty()){
            return new ResponseEntity(tasks, HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/addForParticipant")
    public ResponseEntity addTaskForParticipant(@RequestBody TaskRequest taskRequest){
        if (taskService.addTaskForParticipant(taskRequest)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/addForTeam")
    public ResponseEntity addTaskForTeam(@RequestBody TaskRequest taskRequest){
        if (taskService.addTaskForTeam(taskRequest)) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PutMapping(value="/edit/{taskId}")
    public ResponseEntity editTask(@RequestBody TaskRequest taskRequest, @PathVariable Long taskId){
        if (taskService.editTask(taskRequest, taskId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/remove/{taskId}")
    public ResponseEntity removeTask(@PathVariable Long taskId){
        if (taskService.removeTask(taskId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/editStatus/ToDo/{taskId}")
    public ResponseEntity setToDo(@PathVariable Long taskId){
        if (taskService.setTaskStatusToDo(taskId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/editStatus/InProgress/{taskId}")
    public ResponseEntity setToInProgress(@PathVariable Long taskId){
        if (taskService.setTaskStatusInProgress(taskId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/editStatus/Done/{taskId}")
    public ResponseEntity setToDone(@PathVariable Long taskId){
        if (taskService.setTaskStatusDone(taskId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.CONFLICT);
    }

}
