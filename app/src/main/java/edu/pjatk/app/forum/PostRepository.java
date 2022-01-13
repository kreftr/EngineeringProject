package edu.pjatk.app.forum;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    
    private final EntityManager entityManager;
    
    @Autowired
    public PostRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    
    @Transactional
    public void addPost(Post post) {
        entityManager.persist(post);
    }
    
    @Transactional
    public void deletePost(Post post) {
        entityManager.remove(post);
    }
    
    @Transactional
    public void updatePost(Post post) {
        entityManager.merge(post);
    }
    
    //function to find Post by id that catches no result error
    public Optional<Post> getPostById(Long id) {
        Optional posts;
        try {
            posts = Optional.of(
                    entityManager.createQuery(
                            "SELECT post FROM Post post WHERE post.id = :id", Post.class)
                            .setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            posts = Optional.empty();
        }
        return posts;
    }
    
    public Optional<Post> getPostByTitle(String title) {
        Optional posts;
        try {
            posts = Optional.of(
                    entityManager.createQuery(
                            "SELECT post FROM Post post WHERE post.title = :title", Post.class)
                            .setParameter("title", title).getSingleResult()
                    
            );
        } catch (NoResultException noResultException) {
            posts = Optional.empty();
        }
        return posts;
    }
    
    public Optional<Post> getPostByExactDate(Date date) {
        Optional posts;
        try {
            posts = Optional.of(
                    entityManager.createQuery(
                            "SELECT post from Post post WHERE post.datee = :date", Post.class)
                            .setParameter("date", date).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            posts = Optional.empty();
        }
        return posts;
    }
    
    public Optional<Post> getPostByUserId(Long id) {
        Optional posts;
        try {
            posts = Optional.of(
                    entityManager.createQuery(
                            "SELECT post FROM Post post WHERE post.userr.id = :id", Post.class)
                            .setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            posts = Optional.empty();
        }
        return posts;
    }
    
    public Optional<List<Post>> getAllPosts() {
        Optional<List<Post>> posts;
        
        try {
            posts = Optional.of(entityManager.createQuery(
                    "SELECT post FROM Post post", Post.class)
                    .getResultList());
        } catch (NoResultException noResultException) {
            posts = Optional.empty();
        }
        return posts;
    }
    public Optional<Long> getUserIdFromPost(Long id) {
        Optional<Long> user_id;
        try {
            user_id = Optional.of(
                    entityManager.createQuery(
                            "SELECT user.id FROM User user WHERE user = (SELECT post.userr FROM Post post WHERE post.id = :id)", Long.class)
                            .setParameter("id", id).getSingleResult());
        } catch (NoResultException noResultException) {
            user_id = Optional.empty();
        }
        return user_id;
    }

    public Optional<User> getUser(Long id) {
        Optional<User> user;
        try {
            user = Optional.of(
                    entityManager.createQuery(
                                    "SELECT post.userr FROM Post post WHERE post.id = :id", User.class)
                            .setParameter("id", id).getSingleResult());
        } catch (NoResultException noResultException) {
            user = Optional.empty();
        }
        return user;
    }
    
}
