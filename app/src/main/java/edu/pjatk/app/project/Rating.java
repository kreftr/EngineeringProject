package edu.pjatk.app.project;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.user.profile.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int value;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Rating(int value, Profile profile, Project project){
        this.value = value;
        this.profile = profile;
        this.project = project;
    }

}
