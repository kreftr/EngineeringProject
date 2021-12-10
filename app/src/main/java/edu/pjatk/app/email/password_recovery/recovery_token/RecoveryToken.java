package edu.pjatk.app.email.password_recovery.recovery_token;

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
public class RecoveryToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    private LocalDateTime created;
    private LocalDateTime expired;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public RecoveryToken(String token, LocalDateTime created, LocalDateTime expired, User user) {
        this.token = token;
        this.created = created;
        this.expired = expired;
        this.user = user;
    }

}
