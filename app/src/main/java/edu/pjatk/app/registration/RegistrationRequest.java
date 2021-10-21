package edu.pjatk.app.registration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

}
