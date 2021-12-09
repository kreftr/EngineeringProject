package edu.pjatk.app.project;

import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`project`")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String project_name;
    private String project_description;
    private LocalDateTime creation_date;
    @Enumerated(EnumType.STRING)
    private String project_category;
    private String project_status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_creator")
    private User creator;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_participant")
    List<User> participants;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_file")
    List<File> file;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_task")
    List<Task> task;


    public Project (String project_name, String project_description, LocalDateTime creation_date,
                    String project_category, String project_status, Long project_participant, Long project_creator,
                    Long project_file, Long project_task){
        this.project_name = project_name;
        this.project_description = project_description;
        this.creation_date = creation_date;
        this.project_category = project_category;
        this.project_status = project_status;
    }
}
