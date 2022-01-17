package edu.pjatk.app.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TimestampRequest {

    private String description;
    private Date timeStart;
    private Date timeEnd;
    private String projectName;
    private Long projectId;
}
