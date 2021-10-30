package edu.pjatk.app.user;

import com.sun.istack.Nullable;
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
    @Nullable
    private String name;
    @Nullable
    private String surname;
    private Boolean locked;
    private Boolean enabled;


    public User(String username, String email, String password, LocalDateTime creationDate, UserRole userRole){
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.userRole = userRole;
        this.locked = false;
        this.enabled = false;
    }

}
