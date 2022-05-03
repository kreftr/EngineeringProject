package edu.pjatk.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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

    public Optional<Long> getUsersNumberByUsername(String username) {
        Optional<Long> usersNumber;
        try {
            usersNumber = Optional.of(entityManager.createQuery(
                    "select count(user.id) from User user where user.username like :username", Long.class)
                    .setParameter("username", username+"%").getSingleResult()
            );
        } catch (NoResultException noResultException) {
            usersNumber = Optional.empty();
        }
        return usersNumber;
    }

    public Optional<List<User>> getUsersByUsernameWithPagination(String username, int pageNumber, int pageSize) {
        Optional<List<User>> users;
        try {
            Query query = entityManager.createQuery("select user from User user where user.username like :username");
            query.setParameter("username", username+"%");
            query.setFirstResult((pageNumber-1) * pageSize);
            query.setMaxResults(pageSize);
            users = Optional.of(query.getResultList());
        } catch (NoResultException noResultException) {
            users = Optional.empty();
        }
        return users;
    }

}
