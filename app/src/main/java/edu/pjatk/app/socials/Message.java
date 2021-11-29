package edu.pjatk.app.socials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Date message_date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    public Message(String content, Date message_date, Conversation conversation){
        this.content = content;
        this.message_date = message_date;
        this.conversation = conversation;
    }
}
