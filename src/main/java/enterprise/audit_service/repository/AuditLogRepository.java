package enterprise.audit_service.repository;

import enterprise.audit_service.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    boolean existsByEventId(UUID eventId);

    Page<AuditLog> findByTicketNumberOrderByCreatedAtDesc(
            String ticketNumber,
            Pageable pageable
    );

    Page<AuditLog> findByPerformedByOrderByCreatedAtDesc(
            String performedBy,
            Pageable pageable
    );
}
