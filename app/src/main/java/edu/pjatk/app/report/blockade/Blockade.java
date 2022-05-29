package edu.pjatk.app.report.blockade;

import edu.pjatk.app.report.EntityTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Blockade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private EntityTypeEnum entityType;
    private Long entityId;
    private LocalDateTime creationTime;
    private LocalDateTime endTime;
    private String reasoning;

    public Blockade(Long userId, EntityTypeEnum entityType, Long entityId, LocalDateTime creationTime,
                    LocalDateTime endTime, String reasoning) {
        this.userId = userId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.creationTime = creationTime;
        this.endTime = endTime;
        this.reasoning = reasoning;
    }
}