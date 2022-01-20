package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String creationDate;
    private String expirationDate;

    private Long participantId;
    private String username;
    private String profilePhoto;

    private Long teamId;
    private String teamName;

}
