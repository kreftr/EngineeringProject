package edu.pjatk.app.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private EntityTypeEnum entityType;
    private Long entityId;
    private LocalDateTime creationTime;
    private String reasoning;

    public Report(Long userId, EntityTypeEnum entityType, Long entityId, LocalDateTime creationTime, String reasoning) {
        this.userId = userId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.creationTime = creationTime;
        this.reasoning = reasoning;
    }
}
