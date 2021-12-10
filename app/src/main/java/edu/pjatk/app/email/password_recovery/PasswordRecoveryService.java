package edu.pjatk.app.email.password_recovery;

import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryToken;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryTokenService;
import edu.pjatk.app.request.PasswordChangeRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordRecoveryService {

    @Value("${tokens.recovery-token.validity}")
    private int recoveryTokenValidity;

    private final RecoveryTokenService recoveryTokenService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public PasswordRecoveryService(RecoveryTokenService recoveryTokenService, UserService userService,
                                   EmailService emailService) {
        this.recoveryTokenService = recoveryTokenService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Transactional
    public void sendToken(User user){

        String token = UUID.randomUUID().toString();

        recoveryTokenService.saveRecoveryToken(
                new RecoveryToken(token, LocalDateTime.now(),
                        LocalDateTime.now().plusDays(recoveryTokenValidity), user)
        );

        emailService.send(user.getEmail(), "Password recovery", emailService.emailBuilder(
                user.getUsername(),
                "http://localhost:3000/recovery/"+token,
                "email_password_recovery.html"
        ));
    }

    @Transactional
    public void resetPassword(String token, PasswordChangeRequest passwordChangeRequest){
        RecoveryToken recoveryToken = recoveryTokenService.findRecoveryTokenByToken(token).get();
        User user = recoveryToken.getUser();
        userService.changePassword(user, passwordChangeRequest);
        recoveryTokenService.removeRecoveryToken(recoveryToken);
    }

    public boolean tokenIsValid(String token){
        Optional<RecoveryToken> recoveryToken = recoveryTokenService.findRecoveryTokenByToken(token);
        if (recoveryToken.isPresent()){
            if (LocalDateTime.now().isBefore(recoveryToken.get().getExpired())) return true;
            else return false;
        }
        else return false;
    }

    public boolean tokenAlreadySent(User user){
        Optional<RecoveryToken> recoveryToken = recoveryTokenService.findRecoveryTokenByUser(user);
        return recoveryToken.isPresent();
    }

}
