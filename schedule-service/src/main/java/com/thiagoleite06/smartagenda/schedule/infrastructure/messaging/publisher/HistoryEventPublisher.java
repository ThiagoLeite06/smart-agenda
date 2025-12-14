package com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.publisher;

import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event.ConsultationCompletedEvent;
import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event.ConsultationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.config.RabbitMQProducerConfig.HISTORY_NEW_QUEUE;
import static com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.config.RabbitMQProducerConfig.HISTORY_UPDATE_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishNewConsultation(ConsultationCreatedEvent event) {
        log.info("Publishing consultation created event to history service - Consultation ID: {}", event.getConsultationId());
        try {
            rabbitTemplate.convertAndSend(HISTORY_NEW_QUEUE, event);
            log.info("Successfully published consultation created event - Consultation ID: {}", event.getConsultationId());
        } catch (Exception e) {
            log.error("Error publishing consultation created event - Consultation ID: {}", event.getConsultationId(), e);
        }
    }

    public void publishConsultationUpdate(ConsultationCompletedEvent event) {
        log.info("Publishing consultation completed event to history service - Consultation ID: {}", event.getConsultationId());
        try {
            rabbitTemplate.convertAndSend(HISTORY_UPDATE_QUEUE, event);
            log.info("Successfully published consultation completed event - Consultation ID: {}", event.getConsultationId());
        } catch (Exception e) {
            log.error("Error publishing consultation completed event - Consultation ID: {}", event.getConsultationId(), e);
        }
    }
}
