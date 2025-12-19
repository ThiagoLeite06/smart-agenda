package com.thiagoleite06.smartagenda.notification.usecase;

import com.thiagoleite06.smartagenda.notification.domain.entity.Notification;
import com.thiagoleite06.smartagenda.notification.infrastructure.persistence.repository.NotificationMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetNotificationsByRecipientUseCase {

    private final NotificationMongoRepository repository;

    public List<Notification> execute(String recipient) {
        log.info("Getting notifications for recipient: {}", recipient);

        return repository.findByRecipientOrderByCreatedAtDesc(recipient).stream()
                .map(document -> Notification.builder()
                        .id(document.getId())
                        .recipient(document.getRecipient())
                        .channel(document.getChannel())
                        .subject(document.getSubject())
                        .message(document.getMessage())
                        .status(document.getStatus())
                        .createdAt(document.getCreatedAt())
                        .sentAt(document.getSentAt())
                        .build())
                .collect(Collectors.toList());
    }
}
