package edu.pjatk.app.project.task;

import edu.pjatk.app.file.File;
import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.team.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "task_name")
    private String name;
    @Column(name = "task_description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus status;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    public Task(String name, String description, LocalDateTime expirationDate, Participant participant, Project project){
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.status = TaskStatus.TODO;
        this.expirationDate = expirationDate;
        this.participant = participant;
        this.project = project;
    }

    public Task(String name, String description, LocalDateTime expirationDate, Team team, Project project){
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.status = TaskStatus.TODO;
        this.expirationDate = expirationDate;
        this.team = team;
        this.project = project;
    }

}
