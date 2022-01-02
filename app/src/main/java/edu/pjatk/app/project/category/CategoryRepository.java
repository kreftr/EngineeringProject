package edu.pjatk.app.project.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final EntityManager entityManager;

    @Autowired
    public CategoryRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void save(Category category){
        entityManager.persist(category);
    }

    public void remove(Category category){
        entityManager.remove(category);
    }

    public Optional<Category> getByTitle(String title){
        Optional category;
        try {
            category = Optional.of(entityManager.createQuery(
                    "SELECT category FROM Category category WHERE category.title = :title", Category.class)
                    .setParameter("title", title).getSingleResult()
            );
        } catch (NoResultException e){
            category = Optional.empty();
        }
        return category;
    }

    public Optional<List<Category>> getAll(){
        Optional categories;
        try {
            categories = Optional.of(entityManager.createQuery(
                    "SELECT category FROM Category category", Category.class).getResultList()
            );
        } catch (NoResultException e){
            categories = Optional.empty();
        }
        return categories;
    }

}
