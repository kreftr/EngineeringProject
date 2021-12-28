package edu.pjatk.app.sheduler;

import edu.pjatk.app.email.activation_token.ActivationToken;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryToken;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryTokenService;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableScheduling
public class SchedulersConfig {

    private final ActivationTokenService activationTokenService;
    private final RecoveryTokenService recoveryTokenService;
    private final UserService userService;

    @Autowired
    public SchedulersConfig(ActivationTokenService activationTokenService, RecoveryTokenService recoveryTokenService,
                            UserService userService){
        this.activationTokenService = activationTokenService;
        this.recoveryTokenService = recoveryTokenService;
        this.userService = userService;
    }


    @Scheduled(fixedDelayString = "${scheduler.activation-token.removal}")
    public void unverifiedUsersRemovalScheduler() {

        Optional<List<ActivationToken>> expiredTokens = activationTokenService.findExpiredTokens();

        if (expiredTokens.isPresent() && !expiredTokens.get().isEmpty()){
            for (ActivationToken token : expiredTokens.get()){
                try {
                    userService.removeUser(token.getUser());
                }
                catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        }
    }

    @Scheduled(fixedDelayString = "${scheduler.recovery-token.removal}")
    public void expiredRecoveryTokensRemoval() {

        Optional<List<RecoveryToken>> expiredTokens = recoveryTokenService.findExpiredRecoveryTokens();

        if (expiredTokens.isPresent() && !expiredTokens.get().isEmpty()){
            for (RecoveryToken token : expiredTokens.get()){
                try {
                    recoveryTokenService.removeRecoveryToken(token);
                }
                catch (Exception e){
                    System.out.println("Something went wrong");
                }
            }
        }
    }

}
