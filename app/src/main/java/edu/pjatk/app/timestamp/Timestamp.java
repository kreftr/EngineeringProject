package edu.pjatk.app.timestamp;


import edu.pjatk.app.project.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "timestamp")
public class Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private Date timeStart;
    private Date timeEnd;
    private String projectName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "participant_id")
    private Participant participant;  // participant timestamp belongs to

    public Timestamp(String description, Date timeStart, Date timeEnd, String projectName, Participant participant) {
        this.description = description;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.projectName = projectName;
        this.participant = participant;
    }

}
