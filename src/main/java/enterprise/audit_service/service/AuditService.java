package enterprise.audit_service.service;

import enterprise.audit_service.dto.TicketEvent;
import enterprise.audit_service.entity.AuditLog;
import enterprise.audit_service.repository.AuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void createAuditLog(TicketEvent event) {

        if (auditLogRepository.existsByEventId(event.getEventId())) {
            return;
        }

        AuditLog auditLog = AuditLog.builder()
                .eventId(event.getEventId())
                .ticketId(event.getTicketId())
                .ticketNumber(event.getTicketNumber())
                .eventType(event.getEventType())
                .performedBy(getPerformedBy(event))
                .oldValue(event.getOldStatus())
                .newValue(event.getNewStatus())
                .details(buildDetails(event))
                .build();

        auditLogRepository.save(auditLog);
    }

    private String getPerformedBy(TicketEvent event) {

        if (event.getChangedBy() != null) {
            return event.getChangedBy();
        }

        if (event.getCommentedBy() != null) {
            return event.getCommentedBy();
        }

        return event.getCreatedBy();
    }

    private String buildDetails(TicketEvent event) {

        return switch (event.getEventType()) {

            case "TICKET_CREATED" ->
                    "Ticket created";

            case "TICKET_ASSIGNED" ->
                    "Ticket assigned to " + event.getAssignedTo();

            case "TICKET_STATUS_CHANGED" ->
                    "Status changed from " + event.getOldStatus()
                            + " to " + event.getNewStatus();

            case "TICKET_COMMENT_ADDED" ->
                    "Comment added: " + event.getComment();

            default ->
                    "Ticket event processed";
        };
    }
}