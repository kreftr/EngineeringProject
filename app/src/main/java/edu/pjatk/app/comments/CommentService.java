package edu.pjatk.app.comments;

import edu.pjatk.app.WebApp;
import edu.pjatk.app.forum.Post;
import edu.pjatk.app.forum.PostService;
import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.request.CommentRequest;
import edu.pjatk.app.response.CommentResponse;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(WebApp.class);
    
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
            LOGGER.info("New comment for post : "+ comment.getText() + "," + comment.getDatee() + ",User id :" + comment.getUserr().getId() + ",Post id :" + comment.getPost_id() + ',' + comment.getProject_id());
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
            LOGGER.info("New comment for project : "+ comment.getText() + "," + comment.getDatee() + ",User id :" + comment.getUserr().getId() + ",Post id :" + comment.getPost_id() + ',' + comment.getProject_id());
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
            LOGGER.info("Updated comment: "+ comment.getText() + "," + comment.getDatee() + ",User id :" + comment.getUserr().getId() + ",Post id :" + comment.getPost_id() + ',' + comment.getProject_id());

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
