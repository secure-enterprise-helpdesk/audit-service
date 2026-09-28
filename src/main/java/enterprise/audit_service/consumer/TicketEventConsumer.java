package enterprise.audit_service.consumer;

import enterprise.audit_service.dto.TicketEvent;
import enterprise.audit_service.entity.AuditLog;
import enterprise.audit_service.repository.AuditLogRepository;
import enterprise.audit_service.service.AuditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@AllArgsConstructor
@Slf4j
@Component
public class TicketEventConsumer {

    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    @KafkaListener(
        topics="ticket-events",
        groupId = "audit-service"
    )
    public void consumer(String message) {

            try {
                TicketEvent event =
                        objectMapper.readValue(message, TicketEvent.class);

                auditService.createAuditLog(event);

            } catch (Exception e) {
                log.error("Failed to process Kafka event: {}", message, e);
                throw new RuntimeException("Kafka event processing failed", e);
            }
    }
}
