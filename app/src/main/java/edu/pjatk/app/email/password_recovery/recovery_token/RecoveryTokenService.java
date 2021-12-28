package edu.pjatk.app.email.password_recovery.recovery_token;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecoveryTokenService {

    private final RecoveryTokenRepository recoveryTokenRepository;

    @Autowired
    public RecoveryTokenService(RecoveryTokenRepository recoveryTokenRepository){
        this.recoveryTokenRepository = recoveryTokenRepository;
    }


    public void saveRecoveryToken(RecoveryToken recoveryToken){
        this.recoveryTokenRepository.save(recoveryToken);
    }

    public void removeRecoveryToken(RecoveryToken recoveryToken){
        this.recoveryTokenRepository.remove(recoveryToken);
    }

    public Optional<RecoveryToken> findRecoveryTokenByToken(String token){
        return this.recoveryTokenRepository.findByToken(token);
    }

    public Optional<RecoveryToken> findRecoveryTokenByUser(User user){
        return this.recoveryTokenRepository.findByUser(user);
    }

    public Optional<List<RecoveryToken>> findExpiredRecoveryTokens(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        return recoveryTokenRepository.findExpired(currentDateTime);
    }

}
