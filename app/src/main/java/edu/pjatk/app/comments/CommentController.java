package edu.pjatk.app.comments;

import edu.pjatk.app.forum.PostService;
import edu.pjatk.app.project.ProjectService;
import edu.pjatk.app.request.CommentRequest;
import edu.pjatk.app.response.CommentResponse;
import edu.pjatk.app.response.PostResponse;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.response.project.FullProjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final ProjectService projectService;
    
    @Autowired
    public CommentController(CommentService commentService, PostService postService, ProjectService projectService) {
        this.commentService = commentService;
        this.postService = postService;
        this.projectService = projectService;
    }
    
    @PostMapping(value = "/createCommentPost/{postId}")
    public ResponseEntity createCommentPost(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        Optional<PostResponse> post = postService.findPostById(postId);
        if(post.isEmpty()) {
            return new ResponseEntity(
                    new ResponseMessage("Post does not exist!"), HttpStatus.NOT_FOUND
            );
        }
        commentService.createCommentForPost(commentRequest, postId);
        return new ResponseEntity(
                new ResponseMessage("Comment successfully created!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/createCommentProject/{projectId}")
    public ResponseEntity createCommentProject(@PathVariable Long projectId, @RequestBody CommentRequest commentRequest) {
        Optional<FullProjectResponse> project = projectService.getProjectById(projectId);
        if(project.isEmpty()) {
            return new ResponseEntity(
                    new ResponseMessage("Project does not exist!"), HttpStatus.NOT_FOUND
            );
        }
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

    @GetMapping(value = "/getPostComments/{postId}")
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
