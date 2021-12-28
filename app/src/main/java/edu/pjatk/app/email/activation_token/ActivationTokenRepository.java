package edu.pjatk.app.email.activation_token;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ActivationTokenRepository {

    private final EntityManager entityManager;

    @Autowired
    public ActivationTokenRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    void save(ActivationToken activationToken){
        entityManager.persist(activationToken);
    }

    void remove(ActivationToken activationToken) {
        entityManager.remove(activationToken);
    }

    public void update(ActivationToken activationToken){
        entityManager.merge(activationToken);
    }

    public Optional<ActivationToken> findByToken(String token){
        Optional activationToken;
        try {
            activationToken = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM ActivationToken token WHERE token.token = :token",
                                    ActivationToken.class)
                            .setParameter("token", token).getSingleResult()
            );
        } catch (NoResultException noResultException){
            activationToken = Optional.empty();
        }
        return activationToken;
    }

    public Optional<ActivationToken> findByUser(User user){
        Optional activationToken;
        try {
            activationToken = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM ActivationToken token WHERE token.user = :user",
                                    ActivationToken.class)
                            .setParameter("user", user).getSingleResult()
            );
        } catch (NoResultException noResultException){
            activationToken = Optional.empty();
        }
        return activationToken;
    }

    public Optional<List<ActivationToken>> findExpired(LocalDateTime currentDateTime){
        Optional expiredTokens;
        try {
            expiredTokens = Optional.of(
                    entityManager.createQuery(
                                    "SELECT token FROM ActivationToken token WHERE token.confirmed IS NULL " +
                                            "AND token.expired <= :currentDateTime",
                                    ActivationToken.class)
                            .setParameter("currentDateTime", currentDateTime).getResultList()
            );
        } catch (NoResultException noResultException){
            expiredTokens = Optional.empty();
        }
        return expiredTokens;
    }

}
