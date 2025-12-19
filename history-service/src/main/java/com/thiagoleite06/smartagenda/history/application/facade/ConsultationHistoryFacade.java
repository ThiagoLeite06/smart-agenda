package com.thiagoleite06.smartagenda.history.application.facade;

import com.thiagoleite06.smartagenda.history.infrastructure.messaging.dto.ConsultationEventDto;
import com.thiagoleite06.smartagenda.history.usecase.CreateOrUpdatePreviousConsultationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.thiagoleite06.smartagenda.history.infrastructure.messaging.config.RabbitMQConsumerConfig.HISTORY_NEW_QUEUE;
import static com.thiagoleite06.smartagenda.history.infrastructure.messaging.config.RabbitMQConsumerConfig.HISTORY_UPDATE_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationHistoryFacade {

    private final CreateOrUpdatePreviousConsultationUseCase createOrUpdateUseCase;

    @RabbitListener(queues = HISTORY_NEW_QUEUE)
    public void handleNewConsultation(ConsultationEventDto event) {
        log.info("Received new consultation event from queue - Consultation ID: {}", event.getConsultationId());
        try {
            createOrUpdateUseCase.execute(event);
            log.info("Successfully processed new consultation event - Consultation ID: {}", event.getConsultationId());
        } catch (Exception e) {
            log.error("Error processing new consultation event - Consultation ID: {}", event.getConsultationId(), e);
            throw e; // Let Spring AMQP handle retry
        }
    }

    @RabbitListener(queues = HISTORY_UPDATE_QUEUE)
    public void handleUpdatedConsultation(ConsultationEventDto event) {
        log.info("Received consultation update event from queue - Consultation ID: {}", event.getConsultationId());
        try {
            createOrUpdateUseCase.execute(event);
            log.info("Successfully processed consultation update event - Consultation ID: {}", event.getConsultationId());
        } catch (Exception e) {
            log.error("Error processing consultation update event - Consultation ID: {}", event.getConsultationId(), e);
            throw e; // Let Spring AMQP handle retry
        }
    }
}
