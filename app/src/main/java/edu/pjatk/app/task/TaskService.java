package edu.pjatk.app.task;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
