package com.example.auditservice.service;

import com.example.auditservice.entity.AuditLog;
import com.example.auditservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void saveLog(String eventType, String details) {
        AuditLog log = AuditLog.builder()
                .eventType(eventType)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }
}
