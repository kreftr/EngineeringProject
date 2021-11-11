package edu.pjatk.app.user.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class ProfileRepository {

    private final EntityManager entityManager;

    @Autowired
    public ProfileRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void save(Profile profile){
        entityManager.persist(profile);
    }

    public void update(Profile profile) {
        entityManager.merge(profile);
    }

    public Optional<Profile> findById(Long id){
        Optional profile;
        try {
            profile = Optional.of(
                    entityManager.createQuery(
                                    "SELECT user FROM User user WHERE user.id = :id", Profile.class)
                            .setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException noResultException){
            profile = Optional.empty();
        }
        return profile;
    }

}
