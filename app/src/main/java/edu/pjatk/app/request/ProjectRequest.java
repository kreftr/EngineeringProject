package edu.pjatk.app.request;

import edu.pjatk.app.project.ProjectAccess;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
public class ProjectRequest {

    @Size(max = 46, message = "{validation.project.title.size}")
    @NotBlank(message = "{validation.project.title.notBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "{validation.project.title.regex}")
    private String title;
    @Size(min = 150, max = 300, message = "{validation.project.introduction.size}")
    @NotBlank(message = "{validation.project.introduction.noBlank}")
    private String introduction;
    private String description;
    @NotEmpty(message = "{validation.project.description.category.empty}")
    private Set<String> category;
    private ProjectAccess access;

    @Pattern(regexp = "(www\\.)((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$",
            message = "{validation.project.ytLink.regex}")
    private String youtubeLink;
    @Pattern(regexp = "www\\.facebook\\.com.*", message = "{validation.project.fbLink.regex}")
    private String facebookLink;
    @Pattern(regexp = "www\\.github\\.com.*", message = "{validation.project.gitLink.regex}")
    private String githubLink;
    @Pattern(regexp = "www\\.kickstarter\\.com.*", message = "{validation.project.kickLink.regex}")
    private String kickstarterLink;

}
