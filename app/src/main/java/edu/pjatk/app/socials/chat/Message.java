package edu.pjatk.app.socials.chat;

import edu.pjatk.app.user.User;
import edu.pjatk.app.user.profile.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private LocalDateTime message_date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User user;

    public Message(String content, LocalDateTime message_date, Conversation conversation, User user){
        this.content = content;
        this.message_date = message_date;
        this.conversation = conversation;
        this.user = user;
    }
}
