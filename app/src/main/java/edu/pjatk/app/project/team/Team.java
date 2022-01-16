package edu.pjatk.app.project.team;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "team_participant",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id")}
    )
    private Set<Participant> participants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    public Team(String name, String description, Set<Participant> participants, Project project){
        this.name = name;
        this.description = description;
        this.participants = participants;
        this.project = project;
    }

}
