package edu.pjatk.app.forum;

import edu.pjatk.app.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Size(min = 3, max = 24)
    private String title;
    
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "userr")
    private User userr;

    private LocalDateTime datee;
    
    private String text;
    
    public Post(String title, User userr, LocalDateTime datee, String text){
        this.title = title;
        this.userr = userr;
        this.datee = datee;
        this.text = text;
    }
}
