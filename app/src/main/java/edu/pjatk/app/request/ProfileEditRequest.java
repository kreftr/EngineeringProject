package edu.pjatk.app.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
public class ProfileEditRequest {

    //TODO: Add polish letters to regex żźćó...
    @Pattern(regexp = "(^$)|^[a-zA-Z]+$", message = "{validation.profile.name.regex}")
    @Size(max = 20, message = "{validation.profile.name.size}")
    private String name;
    @Pattern(regexp = "(^$)|^[a-zA-Z]+$", message = "{validation.profile.surname.regex}")
    @Size(max = 20, message = "{validation.profile.surname.size}")
    private String surname;
    @Pattern(regexp = "(^$)|^[a-zA-Z0-9_ ,.'\n-]+$", message = "{validation.profile.bio.regex}")
    @Size(max = 250, message = "{validation.profile.bio.size}")
    private String bio;

    private Set<String> categories;
}
