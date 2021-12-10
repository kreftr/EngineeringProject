package edu.pjatk.app.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordChangeRequest {

    @Size(min = 8, max = 24,message ="{validation.registration.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{1,100}$",
            message ="{validation.registration.password.regex}")
    private String newPassword;

}
