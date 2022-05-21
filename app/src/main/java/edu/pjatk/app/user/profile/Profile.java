package edu.pjatk.app.user.profile;

import edu.pjatk.app.photo.Photo;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.project.Rating;
import edu.pjatk.app.project.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String bio;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "profile_category",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

}
