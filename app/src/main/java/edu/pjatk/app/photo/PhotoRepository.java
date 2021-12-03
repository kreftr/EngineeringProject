package edu.pjatk.app.photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class PhotoRepository {

    private final EntityManager entityManager;

    @Autowired
    public PhotoRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void save(Photo photo){
        entityManager.persist(photo);
    }

    public void remove(Photo photo){
        Photo photoToRemove = entityManager.find(Photo.class, photo.getId());
        entityManager.remove(photoToRemove);
    }

    public Optional<Photo> findById(Long id){
        Optional photo;
        try {
            photo = Optional.of(
                    entityManager.createQuery(
                                    "SELECT photo FROM Photo photo WHERE photo.id = :id", Photo.class)
                            .setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException noResultException){
            photo = Optional.empty();
        }
        return photo;
    }

    public Optional<Photo> findByFileName(String fileName){
        Optional photo;
        try {
            photo = Optional.of(
                    entityManager.createQuery(
                                    "SELECT photo FROM Photo photo WHERE photo.fileName = :fileName", Photo.class)
                            .setParameter("fileName", fileName).getSingleResult()
            );
        } catch (NoResultException noResultException){
            photo = Optional.empty();
        }
        return photo;
    }

}
