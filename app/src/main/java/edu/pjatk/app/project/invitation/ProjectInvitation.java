package edu.pjatk.app.project.invitation;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProjectInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public ProjectInvitation(Project project, User receiver){
        this.project = project;
        this.receiver = receiver;
    }

}
