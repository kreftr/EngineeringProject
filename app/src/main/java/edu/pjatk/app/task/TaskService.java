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

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }

    public Optional<Task> findTaskByName(String task_name) {
        return  taskRepository.findTaskByName(task_name);
    }

    public void createTask(String task_name, String task_status, LocalDateTime expiration_date){
        LocalDateTime date = LocalDateTime.now();
        }
}
