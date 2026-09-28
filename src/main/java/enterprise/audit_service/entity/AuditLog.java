package enterprise.audit_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_ticket", columnList = "ticket_id"),
                @Index(name = "idx_audit_user", columnList = "performed_by"),
                @Index(name = "idx_audit_event", columnList = "event_id"),
                @Index(name = "idx_audit_created", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(name = "ticket_number", nullable = false)
    private String ticketNumber;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
