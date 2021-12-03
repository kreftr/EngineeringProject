package edu.pjatk.app.user.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProfileEditRequest {

    private String name;
    private String surname;
    private String bio;

}
