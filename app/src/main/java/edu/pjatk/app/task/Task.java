package edu.pjatk.app.task;

import edu.pjatk.app.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`task`")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String task_name;
    private String task_description;
    private String task_status;
    private LocalDateTime creation_date;
    private LocalDateTime expiration_date;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(String task_name, String task_description, String task_status, LocalDateTime creation_date,
                LocalDateTime expiration_date) {
        this.task_name = task_name;
        this.task_description = task_description;
        this.task_status = task_status;
        this.creation_date = creation_date;
        this.expiration_date = expiration_date;
    }

    public Task(String task_name, String task_status, LocalDateTime creation_date,
                LocalDateTime expiration_date) {
        this.task_name = task_name;
        this.task_status = task_status;
        this.creation_date = creation_date;
        this.expiration_date = expiration_date;
    }
}
