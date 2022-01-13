package edu.pjatk.app.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class FileRepository {

    private final EntityManager entityManager;

    @Autowired
    public FileRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Optional<File> findById(Long id){
        Optional file;
        try{
            file = Optional.of(
                    entityManager.createQuery("SELECT file FROM File file WHERE file.id=:id")
                            .setParameter("id", id).getSingleResult()
            );
        }
        catch (NoResultException e){
            file = Optional.empty();
        }
        return file;
    }

    public void save(File file){
        entityManager.persist(file);
    }

    public void remove(File file) { entityManager.remove(file); }

    public void update(File file) {
        entityManager.merge(file);
    }

}
