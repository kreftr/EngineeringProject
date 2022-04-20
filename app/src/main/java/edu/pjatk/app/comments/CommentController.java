package edu.pjatk.app.comments;

import edu.pjatk.app.request.CommentRequest;
import edu.pjatk.app.response.CommentResponse;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping(value = "/createCommentPost/{postId}")
    public ResponseEntity createCommentPost(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        commentService.createCommentForPost(commentRequest, postId);
        return new ResponseEntity(
                new ResponseMessage("Comment successfully created!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/createCommentProject/{projectId}")
    public ResponseEntity createCommentProject(@PathVariable Long projectId, @RequestBody CommentRequest commentRequest) {
        commentService.createCommentForProject(commentRequest, projectId);
        return new ResponseEntity(
                new ResponseMessage("Comment successfully created!"), HttpStatus.OK
        );
    }
    
    @GetMapping(value = "/getProjectComments/{projectId}")
    public ResponseEntity<?> getProjectComments(@PathVariable Long projectId) {
        Set<CommentResponse> allComments = commentService.getAllCommentsByProjectId(projectId);
        if(allComments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity(allComments, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getPostComments/{projectId}")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {
        Set<CommentResponse> allComments = commentService.getAllCommentsByPostId(postId);
        if(allComments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity(allComments, HttpStatus.OK);
        }
    }
    
}
