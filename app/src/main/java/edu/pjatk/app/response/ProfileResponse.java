package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {

    private String username;
    private String name;
    private String surname;
    private String bio;
    private String profile_photo;

}
