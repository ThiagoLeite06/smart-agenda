package com.thiagoleite06.smartagenda.notification.application.facade;

import com.thiagoleite06.smartagenda.notification.infrastructure.messaging.dto.NotificationRequest;
import com.thiagoleite06.smartagenda.notification.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.thiagoleite06.smartagenda.notification.infrastructure.messaging.config.NotificationQueueConfig.NOTIFICATION_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private final SendNotificationUseCase sendNotificationUseCase;

    @RabbitListener(queues = NOTIFICATION_QUEUE)
    public void handleNotification(NotificationRequest request) {
        log.info("Received notification request from queue - Recipient: {}, Channel: {}",
                 request.getRecipient(), request.getChannel());
        try {
            sendNotificationUseCase.execute(request);
            log.info("Successfully processed notification request - Recipient: {}", request.getRecipient());
        } catch (Exception e) {
            log.error("Error processing notification request - Recipient: {}", request.getRecipient(), e);
            throw e; // Let Spring AMQP handle retry
        }
    }
}
