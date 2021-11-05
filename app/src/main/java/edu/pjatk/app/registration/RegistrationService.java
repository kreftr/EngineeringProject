package edu.pjatk.app.registration;

import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.email.token.ActivationToken;
import edu.pjatk.app.email.token.ActivationTokenService;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRole;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserService userService;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userService, ActivationTokenService activationTokenService,
                               EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.activationTokenService = activationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void registerUser(RegistrationRequest request){

        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()),
                LocalDateTime.now(), UserRole.USER);

        userService.saveUser(user);

        String token = UUID.randomUUID().toString();

        activationTokenService.saveActivationToken(
                new ActivationToken(token, LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1), user)
        );

        emailService.send(request.getEmail(), emailService.emailBuilder(
                request.getUsername(),
                "http://localhost:8080/registration/verify?token="+token,
                "email_verification.html"
                )
        );
    }

    @Transactional
    public void verifyAccount(ActivationToken activationToken){
        activationTokenService.confirmActivationToken(activationToken);
        userService.activateUser(activationToken.getUser());
    }

}
