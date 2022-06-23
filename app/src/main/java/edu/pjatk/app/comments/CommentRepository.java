package edu.pjatk.app.comments;

import edu.pjatk.app.forum.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository {
    private final EntityManager entityManager;

    @Autowired
    public CommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Transactional
    public void updateComment(Comment comment) {
        entityManager.merge(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = entityManager.find(Comment.class, id);
        entityManager.remove(comment);
    }

    public Optional<List<Comment>> getAllCommentsByPost(Long post_id) {
        Optional<List<Comment>> comments;
        try {
            comments = Optional.of(entityManager.createQuery(
                            "SELECT comment FROM Comment comment WHERE comment.post_id = :post_id ORDER BY comment.datee ASC", Comment.class)
                    .setParameter("post_id", post_id).getResultList());
        } catch (NoResultException noResultException) {
            comments = Optional.empty();
        }
        return comments;
    }

    public Optional<List<Comment>> getAllCommentsByProject(Long project_id) {
        Optional<List<Comment>> comments;
        try {
            comments = Optional.of(entityManager.createQuery(
                            "SELECT comment FROM Comment comment WHERE comment.project_id = :project_id ORDER BY comment.datee ASC", Comment.class)
                    .setParameter("project_id", project_id).getResultList());
        } catch (NoResultException noResultException) {
            comments = Optional.empty();
        }
        return comments;
    }
    
}
