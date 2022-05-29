package edu.pjatk.app.scheduler;

import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.email.activation_token.ActivationToken;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryToken;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryTokenService;
import edu.pjatk.app.recomendations.RecomendationService;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRepository;
import edu.pjatk.app.report.blockade.Blockade;
import edu.pjatk.app.report.blockade.BlockadeService;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableScheduling
public class SchedulersConfig {

    private final ActivationTokenService activationTokenService;
    private final RecoveryTokenService recoveryTokenService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RecomendationService recomendationService;
    private final BlockadeService blockadeService;

    @Autowired
    public SchedulersConfig(ActivationTokenService activationTokenService, RecoveryTokenService recoveryTokenService,
                            UserService userService, UserRepository userRepository, EmailService emailService,
                            RecomendationService recomendationService, BlockadeService blockadeService){
        this.activationTokenService = activationTokenService;
        this.recoveryTokenService = recoveryTokenService;
        this.userService = userService;
        this.blockadeService = blockadeService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.recomendationService = recomendationService;
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

    @Scheduled(cron = "0 0 17 10 * * ")  // 17.00, 10th day every month */10 * * * * *
    public void sendEmailNotifications() {
        Optional<List<Long>> listOfIds = userRepository.getAllUsersIdWithNotificationOn();

        if (listOfIds.isPresent() && !listOfIds.get().isEmpty()){
            for (Long id: listOfIds.get())
            {
                Optional<User> user = userRepository.findById(id);
                if (user.isEmpty()) { continue; }

                String recommendedLink = recomendationService.getRecomendedProjectLink(user.get());

                emailService.send(user.get().getEmail(), "We got something that may interest you",
                        "Hi " + user.get().getUsername() + ". We found project suitable for your " +
                                "interests. Click link below if you want to know more about it" + "\n" + recommendedLink
                );
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")  // backup every day, at 12pm UTC
    public void databaseBackup() throws IOException, InterruptedException {

        String base_project_path = System.getProperty("user.dir");  // project root path
        String backup_script_path = base_project_path + "/app/db_backup.sh";  // path to db backup script
        String backups_folder_path = base_project_path + "/backups";  // backups root folder

        // create backup file structure if not exist yet
        Files.createDirectories(Paths.get(backups_folder_path));
        Files.createDirectories(Paths.get(backups_folder_path + "/database"));
        Files.createDirectories(Paths.get(backups_folder_path + "/uploads"));

        // add necessary permissions to database backup file
        Process permission_process = new ProcessBuilder("chmod", "u+x", backup_script_path).start();
        permission_process.waitFor();
        permission_process.destroy();

        // backup database
        Process backup_process = new ProcessBuilder(backup_script_path,
                backups_folder_path + "/database").start();
        backup_process.waitFor();
        backup_process.destroy();

        // backup uploaded files
        try {
            File upload_source = new File(base_project_path + "/uploads");
            File upload_destination = new File(backups_folder_path + "/uploads");
            FileUtils.copyDirectory(upload_source, upload_destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelayString = "${scheduler.recovery-token.removal}")
    public void unblockLockedUsers(){
        List<Blockade> blockades = blockadeService.getAllUsers();
        for (Blockade b : blockades) {
            if (LocalDateTime.now().isAfter(b.getEndTime())) {
                User user = userService.findUserById(b.getUserId()).get();
                user.setLocked(false);
                userService.updateUser(user);
            }
        }
    }

}
