package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectJoinRequestResponse {

    private Long pendingId;
    private Long userId;
    private String username;
    private String profilePhoto;
    private Long projectId;
    private String projectTitle;
}
