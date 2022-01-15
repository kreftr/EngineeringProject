package edu.pjatk.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class TeamRequest {

    private Long projectId;
    private String name;
    private String description;
    private Set<String> usernames;

}
