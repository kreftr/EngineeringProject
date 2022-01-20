package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TimestampResponse {

    private Long id;
    private String description;
    private Date timeStart;
    private Date timeEnd;
    private String projectName;
    private Long participantId;

}
