package edu.pjatk.app.sheduler;

import edu.pjatk.app.email.activation_token.ActivationToken;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryToken;
import edu.pjatk.app.email.password_recovery.recovery_token.RecoveryTokenService;
import edu.pjatk.app.user.UserService;
import liquibase.pro.packaged.S;
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
import java.time.format.DateTimeFormatter;
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

//    @Scheduled(fixedDelayString = "1000")  // creates backup every second, debug option
    @Scheduled(cron = "0 0 0 * * *")  // backup every day, at 12pm UTC
    public void databaseBackup() throws IOException, InterruptedException {
        String docker_container = "af221c801274";

        String base_project_path = System.getProperty("user.dir") + "/EngineeringProject/";  // project root path
        String backup_script_path = base_project_path + "app/db_backup.sh";  // path to db backup script
        String backups_folder_path = base_project_path + "backups";  // backups root folder

        // create backup file structure if not exist yet
        Files.createDirectories(Paths.get(backups_folder_path));
        Files.createDirectories(Paths.get(backups_folder_path + "/database"));
        Files.createDirectories(Paths.get(backups_folder_path + "/uploads"));

        // add necessary permissions to database backup file
        Process permission_process = new ProcessBuilder("chmod", "u+x", backup_script_path).start();
        permission_process.waitFor();
        permission_process.destroy();

        // backup database
        Process backup_process = new ProcessBuilder(backup_script_path, docker_container,
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
}
