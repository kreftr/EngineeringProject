package edu.pjatk.app.forum;

import edu.pjatk.app.request.PostRequest;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @PostMapping("/addPost")
    public ResponseEntity addPost(@RequestBody PostRequest postRequest) {
        postService.addPost(postRequest);
        return new ResponseEntity(
                new ResponseMessage("Post successfully created!"), HttpStatus.OK
        );
    }
    @PostMapping("/deletePost")
    public ResponseEntity deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
        return new ResponseEntity(
                new ResponseMessage("Post successfully deleted!"), HttpStatus.OK
        );
    }
    
    @GetMapping(value = "/getAllPosts")
    public ResponseEntity<?> getAllPosts() {
        Optional<List<Post>> allPosts = postService.getAllPosts();
        if(!allPosts.isEmpty()) {
            return new ResponseEntity<>(
                    allPosts, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no projects!"), HttpStatus.NOT_FOUND
            );
        }
    }
    
    @GetMapping(value = "/getUserFromPost/{id}")
    public ResponseEntity<?> getUserIdFromPost(@PathVariable Long id) {
        Optional<Long> user_id = postService.getUserIdFromPost(id);
        if(user_id.isPresent()) {
            return new ResponseEntity(
              user_id, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity(
                    new ResponseMessage("There is no user with that post id"), HttpStatus.NOT_FOUND
            );
        }
    }
    
    @GetMapping(value = "/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = postService.getUser(id);
        if(user.isPresent()) {
            return new ResponseEntity<>(
                    user, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Couldn't find that user"), HttpStatus.NOT_FOUND
            );
        }
    }
}
