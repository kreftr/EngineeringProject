package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class MiniProjectResponse {

    private Long projectId;
    private String projectPhoto;
    private String title;
    private String introduction;
    private Set<String> categories;
    private String creationDate;

    private Long authorId;
    private String authorUsername;
    private String authorPhoto;
}
