package edu.pjatk.app.email.password_recovery.recovery_token;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class RecoveryTokenRepository {

    private final EntityManager entityManager;

    @Autowired
    public RecoveryTokenRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void save(RecoveryToken recoveryToken){
        entityManager.persist(recoveryToken);
    }

    public void remove(RecoveryToken recoveryToken){
        entityManager.remove(recoveryToken);
    }

    public Optional<RecoveryToken> findByToken(String token){
        Optional recoveryToken;
        try {
            recoveryToken = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM RecoveryToken token WHERE token.token = :token", RecoveryToken.class)
                            .setParameter("token", token).getSingleResult()
            );
        } catch (NoResultException noResultException){
            recoveryToken = Optional.empty();
        }
        return recoveryToken;
    }

    public Optional<RecoveryToken> findByUser(User user){
        Optional recoveryToken;
        try {
            recoveryToken = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM RecoveryToken token WHERE token.user = :user", RecoveryToken.class)
                            .setParameter("user", user).getSingleResult()
            );
        } catch (NoResultException noResultException){
            recoveryToken = Optional.empty();
        }
        return recoveryToken;
    }

    public Optional<List<RecoveryToken>> findExpired(LocalDateTime currentDateTime){
        Optional expiredRecoveryTokens;
        try {
            expiredRecoveryTokens = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM RecoveryToken token WHERE token.expired <= :currentDateTime", RecoveryToken.class)
                            .setParameter("currentDateTime", currentDateTime).getResultList()
            );
        } catch (NoResultException noResultException){
            expiredRecoveryTokens = Optional.empty();
        }
        return expiredRecoveryTokens;
    }

}
