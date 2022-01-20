package edu.pjatk.app.project;

import edu.pjatk.app.file.File;
import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.project.task.Task;
import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private String project_introduction;
    private String project_description;
    private LocalDateTime creation_date;
    @Enumerated(EnumType.STRING)
    private ProjectStatus project_status;
    @Enumerated(EnumType.STRING)
    private ProjectAccess project_access;
    private String youtube_link;
    private String facebook_link;
    private String github_link;
    private String kickstarter_link;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "project_category",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Rating> ratings =  new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Participant> participants = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_creator")
    private User creator;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> task = new ArrayList<>();


    public Project(String project_name, String project_introduction, String project_description,
                   LocalDateTime creation_date, Set<Category> categories, ProjectStatus project_status,
                   ProjectAccess project_access, String youtube_link, String facebook_link, String github_link,
                   String kickstarter_link, User creator, Photo photo){
        this.project_name = project_name;
        this.project_introduction = project_introduction;
        this.project_description = project_description;
        this.creation_date = creation_date;
        this.categories = categories;
        this.project_status = project_status;
        this.project_access = project_access;
        this.youtube_link = youtube_link;
        this.facebook_link = facebook_link;
        this.github_link = github_link;
        this.kickstarter_link = kickstarter_link;
        this.creator = creator;
        this.photo = photo;
    }

}