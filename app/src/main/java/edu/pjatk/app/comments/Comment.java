package edu.pjatk.app.comments;

import edu.pjatk.app.forum.Post;
import edu.pjatk.app.project.Project;
import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String text;

    private LocalDateTime datee;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userr")
    private User userr;
    
    private Long post_id;
    
    private Long project_id;
    
    public Comment(String text, LocalDateTime datee, User userr, Long post_id, Long project_id) {
        this.text = text;
        this.datee = datee;
        this.userr = userr;
        this.post_id = post_id;
        this.project_id = project_id;
    }
}
