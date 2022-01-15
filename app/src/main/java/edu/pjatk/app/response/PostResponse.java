package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
    private Long postId;
    private String title;
    private String text;
    private String datee;
    
    private Long userId;
    private String userName;
    private String userPhoto;
}
