package edu.pjatk.app.email.token;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

}
