package edu.pjatk.app.task;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.getTaskById(id);
    }

    public Optional<Task> getTaskByName(String task_name) {
        return  taskRepository.getTaskByName(task_name);
    }


    public void createTask(String task_name, String task_description, String task_status, LocalDateTime expiration_date){
        LocalDateTime date = LocalDateTime.now();
        Task task = new Task(task_name, task_description,task_status, date, expiration_date);
        taskRepository.createTask(task);
        }

    public void createTask(String task_name, String task_status, LocalDateTime expiration_date){
        LocalDateTime date = LocalDateTime.now();
        Task task = new Task(task_name,task_status, date, expiration_date);
        taskRepository.createTask(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    public void editTaskName(Long id, String task_name) {
        Optional<Task> task = taskRepository.getTaskById(id);
        if (task.isPresent()){
            taskRepository.editTaskName(id, task_name);
        }
    }

}
