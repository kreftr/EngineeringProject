package edu.pjatk.app.socials.friends;

import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean pending;

    @OneToOne
    @JoinColumn(name = "first_user_id", referencedColumnName = "id")
    User firstUser;

    @OneToOne
    @JoinColumn(name = "second_user_id", referencedColumnName = "id")
    User secondUser;

    public Friend(User firstUser, User secondUser){
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.pending = true;
    }
}
