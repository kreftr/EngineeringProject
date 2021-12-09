package edu.pjatk.app.email.token;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;

    @Autowired
    public ActivationTokenService(ActivationTokenRepository activationTokenRepository) {
        this.activationTokenRepository = activationTokenRepository;
    }


    public void saveActivationToken(ActivationToken activationToken){
        activationTokenRepository.save(activationToken);
    }

    @Transactional
    public void removeActivationTokenByUser(User user){
        ActivationToken activationToken = activationTokenRepository.findByUser(user).get();
        activationTokenRepository.remove(activationToken);
    }

    public Optional<ActivationToken> findActivationTokenByToken(String token){
        return activationTokenRepository.findByToken(token);
    }

    public void confirmActivationToken(ActivationToken activationToken){
        activationToken.setConfirmed(LocalDateTime.now());
        activationTokenRepository.update(activationToken);
    }

}
