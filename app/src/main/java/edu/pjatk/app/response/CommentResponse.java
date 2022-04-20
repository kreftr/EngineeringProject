package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    //Comment
    private Long commentId;
    private String text;
    private String datee;

    //User
    private Long userId;
    private String userName;
    private String userPhoto;
    
    //Post
    private Long postId;
    
    //Project
    private Long projectId;
}
