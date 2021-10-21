package edu.pjatk.app.registration;

import edu.pjatk.app.model.User;
import edu.pjatk.app.model.UserRole;
import edu.pjatk.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userService, BCryptPasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegistrationRequest request){
        userService.saveUser(
                new User(request.getUsername(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        UserRole.USER)
        );
    }

    public boolean thatUsernameAlreadyExists(String username){
        if (userService.findUserByUsername(username).isPresent()) return true;
        else return false;
    }

    public boolean thatEmailAlreadyExists(String email){
        if (userService.findUserByEmail(email).isPresent()) return true;
        else return false;
    }

}
