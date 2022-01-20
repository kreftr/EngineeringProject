package edu.pjatk.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskRequest {

    private Long projectId;
    private String name;
    private String description;
    private String expirationDate;
    private Long userId;
    private Long teamId;

}
