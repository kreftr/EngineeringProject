package edu.pjatk.app.forum;

import edu.pjatk.app.WebApp;
import edu.pjatk.app.request.PostRequest;
import edu.pjatk.app.request.ProjectRequest;
import edu.pjatk.app.response.PostResponse;
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
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(WebApp.class);
    
    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }
    
    @Transactional
    public void addPost(PostRequest postRequest) {
        
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        
        Post post = new Post(
                postRequest.getTitle(), loggedUser.get(), 
                LocalDateTime.now(), postRequest.getText()
        );
        postRepository.addPost(post);
        LOGGER.info("New Post : "+ post.getTitle() + ",User id:" + post.getUserr().getId() + "," + post.getDatee() + "," + post.getText());
    }
    
    @Transactional
    public void deletePost(Post post) {
        postRepository.deletePost(post);
    }
    
    public boolean deletePostById(Long id) {
        Optional<Post> post = postRepository.getPostById(id);
        if (post.isPresent()) {
            postRepository.deletePost(post.get());
            return true;
        }
        else return false;
    }
    
    public void updatePost(Post post) {
        postRepository.updatePost(post);
        LOGGER.info("Edited Post : "+ post.getTitle() + "," + post.getUserr().getId() + "," + post.getDatee() + "," + post.getText());
    }
    
    public Optional<PostResponse> findPostById(Long id) {
        Optional<Post> post = postRepository.getPostById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (post.isEmpty()) { return Optional.empty(); }

        String userPhoto;
        if (post.get().getUserr().getProfile().getPhoto() != null) {
            userPhoto = post.get().getUserr().getProfile().getPhoto().getFileName();
        } else {
            userPhoto = null;
        }

        return Optional.of(new PostResponse(post.get().getId(), post.get().getTitle(), post.get().getText(),
                post.get().getDatee().format(formatter), post.get().getUserr().getId(), post.get().getUserr().getUsername(), userPhoto));
    }
    
    public Optional<Post> findPostByTitle(String title) {
        return postRepository.getPostByTitle(title);
    }
    
    public Optional<Post> findPostByDate(Date date) {
        return postRepository.getPostByExactDate(date);
    }
    
    public Optional<Post> findPostByUserId(Long id) {
        return postRepository.getPostByUserId(id);
    }
    
    public Set<PostResponse> getAllPosts() {
        Set<PostResponse> postResponses = new HashSet<>();
        Optional<List<Post>> posts = postRepository.getAllPosts();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        if (posts.isPresent()) {
            for (Post p : posts.get()) {

                String userPhoto;
                if (p.getUserr().getProfile().getPhoto() != null) {
                    userPhoto = p.getUserr().getProfile().getPhoto().getFileName();
                } else {
                    userPhoto = null;
                }

                postResponses.add(
                        new PostResponse(
                                p.getId(), p.getTitle(), p.getText(), p.getDatee().format(formatter),
                                p.getUserr().getId(), p.getUserr().getUsername(), userPhoto
                        )
                );
            }
            return postResponses;
        } else {
            return Collections.emptySet();
        }
    }

    public List<PostResponse> getRecentPosts() {
        List<PostResponse> postResponses = new ArrayList<>();
        Optional<List<Post>> posts = postRepository.getRecentPosts();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (posts.isPresent()) {
            for (Post p : posts.get()) {

                String userPhoto;
                if (p.getUserr().getProfile().getPhoto() != null) {
                    userPhoto = p.getUserr().getProfile().getPhoto().getFileName();
                } else {
                    userPhoto = null;
                }

                postResponses.add(
                        new PostResponse(
                                p.getId(), p.getTitle(), p.getText(), p.getDatee().format(formatter),
                                p.getUserr().getId(), p.getUserr().getUsername(), userPhoto
                        )
                );
            }
            return postResponses;
        } else {
            return Collections.emptyList();
        }
    }
    
    public Optional<Long> getUserIdFromPost(Long id) {
        return postRepository.getUserIdFromPost(id);
    } 
    
    public Optional<User> getUser(Long id) {
        return postRepository.getUser(id);
    }
}
