package edu.pjatk.app.repository;

import edu.pjatk.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public class UserRepository {

    private final EntityManager entityManager;

    @Autowired
    public UserRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void save(User user){
        entityManager.persist(user);
    }

    public Optional<User> findByUsername(String username){
        Optional user;
        try {
            user = Optional.of(
                    entityManager.createQuery(
                            "SELECT user FROM User user WHERE user.username = :username", User.class)
                            .setParameter("username", username).getSingleResult()
            );
        } catch (NoResultException noResultException){
            user = Optional.empty();
        }
        return user;
    }

    public Optional<User> findByEmail(String email){
        Optional user;
        try {
            user = Optional.of(
                    entityManager.createQuery(
                                    "SELECT user FROM User user WHERE user.email = :email", User.class)
                            .setParameter("email", email).getSingleResult()
            );
        } catch (NoResultException noResultException){
            user = Optional.empty();
        }
        return user;
    }

}
