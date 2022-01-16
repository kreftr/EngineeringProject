package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class TeamResponse {

    private Long teamId;
    private String name;
    private String description;
    private Set<MemberResponse> members;

}
