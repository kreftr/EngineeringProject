package edu.pjatk.app.request;

import edu.pjatk.app.project.ProjectAccess;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;


@Getter
@Setter
public class ProjectRequest {

    private String title;
    private String introduction;
    private String description;
    private Set<String> category;
    private ProjectAccess access;

    private String youtubeLink;
    private String facebookLink;
    private String githubLink;
    private String kickstarterLink;

}
