package edu.pjatk.app.socials.chat;

import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_user_id")
    private User first_user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_user_id")
    private User second_user;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.REMOVE)
    private List<Message> messages;

    public Conversation(User first_user, User second_user){
        this.first_user = first_user;
        this.second_user = second_user;
    }
}
