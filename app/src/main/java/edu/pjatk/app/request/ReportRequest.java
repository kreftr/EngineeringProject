package edu.pjatk.app.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {

    private Long entityId;
    private String entityType;
    private String reasoning;
}
