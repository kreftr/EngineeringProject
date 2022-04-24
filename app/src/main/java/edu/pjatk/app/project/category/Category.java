package edu.pjatk.app.project.category;

import edu.pjatk.app.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    private Set<Project> profiles = new HashSet<>();

    public Category(String title){
        this.title = title;
    }

}
