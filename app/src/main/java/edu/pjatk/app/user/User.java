package edu.pjatk.app.user;

import edu.pjatk.app.project.participant.Participant;
import edu.pjatk.app.user.profile.Profile;
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
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private Boolean locked;
    private Boolean enabled;
    private Boolean emailNotification;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public User(String username, String email, String password, LocalDateTime creationDate,
                UserRole userRole, Profile profile){
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.userRole = userRole;
        this.profile =  profile;
        this.locked = false;
        this.enabled = false;
    }

    //Participant = what projects the user participates in and what permissions he has
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Participant> participants = new HashSet<>();

}
