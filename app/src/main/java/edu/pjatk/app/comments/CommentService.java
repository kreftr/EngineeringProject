package edu.pjatk.app.comments;

import edu.pjatk.app.forum.Post;
import edu.pjatk.app.forum.PostService;
import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.request.CommentRequest;
import edu.pjatk.app.response.CommentResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserService userService;
    
    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }
    
    //section for control of Comments (Create, Update, Delete)
    @Transactional
    public void createCommentForPost(CommentRequest commentRequest, Long postId) {
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        
        if(loggedUser.isPresent()) {
            Comment comment = new Comment(
                    commentRequest.getText(),
                    LocalDateTime.now(),
                    loggedUser.get(),
                    postId,
                    null
                    
            );
            
            commentRepository.createComment(comment);
        }
    }

    public void createCommentForProject(CommentRequest commentRequest, Long projectId) {
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if(loggedUser.isPresent()) {
            Comment comment = new Comment(
                    commentRequest.getText(),
                    LocalDateTime.now(),
                    loggedUser.get(),
                    null, 
                    projectId
            );
            
            commentRepository.createComment(comment);
        }
    }
    
    @Transactional
    public void updateComment(CommentRequest commentRequest, Long postId, Long projectId, Long id) {
        
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        
        if(loggedUser.isPresent()) {
            Comment comment = new Comment(
                    commentRequest.getText(),
                    LocalDateTime.now(),
                    loggedUser.get(),
                    postId,
                    projectId
            );
            comment.setId(id);
            commentRepository.updateComment(comment);
        }
    }
    
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteComment(id);
    }
    
    @Transactional
    public Set<CommentResponse> getAllCommentsByPostId(Long postId) {
        Set<CommentResponse> commentResponses = new HashSet<>();
        Optional<List<Comment>> comments = commentRepository.getAllCommentsByPost(postId);
        
        //format date to Pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        if (comments.isPresent()) {

            for (Comment c : comments.get()) {

                String userPhoto;
                if (c.getUserr().getProfile().getPhoto() != null) {
                    userPhoto = c.getUserr().getProfile().getPhoto().getFileName();
                } else {
                    userPhoto = null;
                }

                commentResponses.add(
                        new CommentResponse(
                                c.getId(), c.getText(), c.getDatee().format(formatter),
                                c.getUserr().getId(), c.getUserr().getUsername(), userPhoto,
                                c.getPost_id(), c.getProject_id()
                        )
                );
            }
            return commentResponses;
        } else {
            return Collections.emptySet();
        }
    }
    
    @Transactional
    public Set<CommentResponse> getAllCommentsByProjectId(Long projectId) {
        Set<CommentResponse> commentResponses = new HashSet<>();
        Optional<List<Comment>> comments = commentRepository.getAllCommentsByProject(projectId);

        //format date to Pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (comments.isPresent()) {

            for (Comment c : comments.get()) {
                String userPhoto;
                if (c.getUserr().getProfile().getPhoto() != null) {
                    userPhoto = c.getUserr().getProfile().getPhoto().getFileName();
                } else {
                    userPhoto = null;
                }

                commentResponses.add(
                        new CommentResponse(
                                c.getId(), c.getText(), c.getDatee().format(formatter),
                                c.getUserr().getId(), c.getUserr().getUsername(), userPhoto,
                                c.getPost_id(), c.getProject_id()
                        )
                );
                
            }
            return commentResponses;
        } else return Collections.emptySet();
    }
}
