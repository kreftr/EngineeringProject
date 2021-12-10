package edu.pjatk.app.registration;

import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.email.activation_token.ActivationToken;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.request.RegistrationRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRole;
import edu.pjatk.app.user.UserService;
import edu.pjatk.app.user.profile.Profile;
import edu.pjatk.app.user.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

    @Value("${tokens.activation-token.validity}")
    private int activationTokenValidity;

    private final UserService userService;
    private final ProfileService profileService;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userService, ProfileService profileService,
                               ActivationTokenService activationTokenService, EmailService emailService,
                               BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.profileService = profileService;
        this.activationTokenService = activationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    //TODO: Token validity should be read from the configuration file
    @Transactional
    public void registerUser(RegistrationRequest request){

        Profile profile = new Profile();
        profileService.saveProfile(profile);

        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()),
                LocalDateTime.now(), UserRole.USER, profile);

        userService.saveUser(user);

        String token = UUID.randomUUID().toString();

        activationTokenService.saveActivationToken(
                new ActivationToken(token, LocalDateTime.now(),
                        LocalDateTime.now().plusDays(activationTokenValidity), user)
        );

        emailService.send(request.getEmail(), "Account activation", emailService.emailBuilder(
                request.getUsername(),
                "http://localhost:3000/verification/"+token,
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
