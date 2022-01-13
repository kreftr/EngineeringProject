package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponse {

    private Long userId;
    private String username;
    private String profilePhoto;
    private String projectRole;

}
