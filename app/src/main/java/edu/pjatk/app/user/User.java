package edu.pjatk.app.user;

import edu.pjatk.app.user.profile.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


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

    @OneToOne(fetch = FetchType.LAZY)
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

}
