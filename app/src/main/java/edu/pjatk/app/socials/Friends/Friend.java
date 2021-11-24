package edu.pjatk.app.socials.Friends;

import edu.pjatk.app.socials.Conversation;
import edu.pjatk.app.socials.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`friend`")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean pending;
    private Long first_user_id;
    private Long second_user_id;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "second_user_id")
//    private User user;
}
