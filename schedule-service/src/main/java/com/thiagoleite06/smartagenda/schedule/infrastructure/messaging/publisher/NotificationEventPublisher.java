package com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.publisher;

import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.config.RabbitMQProducerConfig.NOTIFICATION_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishNotification(NotificationRequest notification) {
        log.info("Publishing notification to queue - Recipient: {}, Channel: {}",
                 notification.getRecipient(), notification.getChannel());
        try {
            rabbitTemplate.convertAndSend(NOTIFICATION_QUEUE, notification);
            log.info("Successfully published notification - Recipient: {}", notification.getRecipient());
        } catch (Exception e) {
            log.error("Error publishing notification - Recipient: {}", notification.getRecipient(), e);
        }
    }
}
