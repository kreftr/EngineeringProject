package edu.pjatk.app.file;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.task.Task;
import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "file_name")
    private String name;
    @Column(name = "file_url")
    private String url;
    @Column(name = "file_size")
    private Long size;
    @Column(name="upload_date")
    private LocalDateTime uploadDate;
    @Column(name="is_locked")
    private Boolean isLocked;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public File(String name, String url, Long size, LocalDateTime uploadDate, Project project, User user){
        this.name = name;
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
        this.project = project;
        this.user = user;
        this.isLocked = false;
    }

    public File(String name, String url, Long size, LocalDateTime uploadDate, Project project, User user, Task task){
        this.name = name;
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
        this.project = project;
        this.user = user;
        this.task = task;
        this.isLocked = false;
    }

}
