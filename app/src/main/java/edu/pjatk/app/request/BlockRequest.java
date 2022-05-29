package edu.pjatk.app.request;

import edu.pjatk.app.report.EntityTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockRequest {

    private Long reportId;
    private EntityTypeEnum entityType;
    private Long entityId;
    private Long daysOfBlockade;
    private String reasoning;
}
