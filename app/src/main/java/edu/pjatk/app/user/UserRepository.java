package edu.pjatk.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

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

    public void remove(User user){
        entityManager.remove(user);
    }

    public void update(User user){
        entityManager.merge(user);
    }

    public Optional<User> findById(Long id){
        Optional user;
        try {
            user = Optional.of(
                    entityManager.createQuery(
                                    "SELECT user FROM User user WHERE user.id = :id", User.class)
                            .setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException noResultException){
            user = Optional.empty();
        }
        return user;
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

    public Optional<List<User>> findBySimilarUsername(String username){
        Optional users;
        try {
            users = Optional.of(
                    entityManager.createQuery(
                                    "SELECT user FROM User user WHERE user.username LIKE :username", User.class)
                            .setParameter("username", username+"%").getResultList()
            );
        } catch (NoResultException noResultException){
            users = Optional.empty();
        }
        return users;
    }

    public Optional<List<Long>> getAllUsersIdWithNotificationOn(){
        Optional listOfIds;
        try {
            listOfIds = Optional.of(
                    entityManager.createQuery(
                                    "SELECT user.id FROM User user WHERE user.emailNotification = true", Long.class)
                            .getResultList()
            );
        } catch (NoResultException noResultException){
            listOfIds = Optional.empty();
        }
        return listOfIds;
    }

}
