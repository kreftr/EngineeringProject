package edu.pjatk.app.project.team;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToMany(mappedBy = "teams")
    private Set<Participant> participants;

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
