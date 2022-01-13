package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvitationResponse {

    private Long invitationId;
    private Long projectId;
    private String projectPhoto;
    private String projectTitle;

}
