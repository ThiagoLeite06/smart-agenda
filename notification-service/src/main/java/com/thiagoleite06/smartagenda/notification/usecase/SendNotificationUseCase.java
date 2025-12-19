package com.thiagoleite06.smartagenda.notification.usecase;

import com.thiagoleite06.smartagenda.notification.domain.entity.Notification;
import com.thiagoleite06.smartagenda.notification.infrastructure.messaging.dto.NotificationRequest;
import com.thiagoleite06.smartagenda.notification.infrastructure.persistence.document.NotificationDocument;
import com.thiagoleite06.smartagenda.notification.infrastructure.persistence.repository.NotificationMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {

    private final NotificationMongoRepository repository;

    public Notification execute(NotificationRequest request) {
        log.info("Processing notification - Recipient: {}, Channel: {}", request.getRecipient(), request.getChannel());

        // Create notification document
        NotificationDocument document = NotificationDocument.builder()
                .recipient(request.getRecipient())
                .channel(request.getChannel())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("PENDING")
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now())
                .build();

        // Simulate sending (in real scenario, this would call email/SMS/WhatsApp APIs)
        try {
            log.info("Simulating notification send via {} to {}", request.getChannel(), request.getRecipient());

            // Mark as sent
            document.setStatus("SENT");
            document.setSentAt(LocalDateTime.now());

            log.info("Notification sent successfully - Recipient: {}", request.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send notification - Recipient: {}", request.getRecipient(), e);
            document.setStatus("FAILED");
        }

        // Save to MongoDB
        document = repository.save(document);
        log.info("Notification saved to MongoDB - ID: {}", document.getId());

        return convertToDomain(document);
    }

    private Notification convertToDomain(NotificationDocument document) {
        return Notification.builder()
                .id(document.getId())
                .recipient(document.getRecipient())
                .channel(document.getChannel())
                .subject(document.getSubject())
                .message(document.getMessage())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .sentAt(document.getSentAt())
                .build();
    }
}
