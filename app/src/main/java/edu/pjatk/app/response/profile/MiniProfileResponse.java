package edu.pjatk.app.response.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MiniProfileResponse {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private String profile_photo;

}
