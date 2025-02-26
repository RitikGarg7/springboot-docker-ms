package com.example.auditservice.kafka;

import com.example.auditservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogConsumer {

    private final AuditLogService auditLogService;

    @KafkaListener(topics = "audit-logs", groupId = "audit-group")
    public void consume(String message) {
        auditLogService.saveLog("AUDIT_EVENT", message);
        System.out.println("Received audit log: " + message);
    }
}
