package com.thiagoleite06.smartagenda.history.usecase;

import com.thiagoleite06.smartagenda.history.domain.entity.PreviousConsultation;
import com.thiagoleite06.smartagenda.history.infrastructure.messaging.dto.ConsultationEventDto;
import com.thiagoleite06.smartagenda.history.infrastructure.persistence.entity.PreviousConsultationEntity;
import com.thiagoleite06.smartagenda.history.infrastructure.persistence.repository.PreviousConsultationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrUpdatePreviousConsultationUseCase {

    private final PreviousConsultationJpaRepository repository;

    @Transactional
    public PreviousConsultation execute(ConsultationEventDto event) {
        log.info("Processing consultation event - Consultation ID: {}", event.getConsultationId());

        // Check if already exists
        PreviousConsultationEntity entity = repository.findByConsultationId(event.getConsultationId())
                .orElse(null);

        if (entity == null) {
            // Create new
            log.info("Creating new previous consultation record - Consultation ID: {}", event.getConsultationId());
            entity = PreviousConsultationEntity.builder()
                    .consultationId(event.getConsultationId())
                    .patientId(event.getPatientId())
                    .patientName(event.getPatientName())
                    .patientEmail(event.getPatientEmail())
                    .doctorId(event.getDoctorId())
                    .doctorName(event.getDoctorName())
                    .doctorSpecialty(event.getDoctorSpecialty())
                    .status(event.getStatus())
                    .scheduledAt(event.getScheduledAt())
                    .completedAt(event.getCompletedAt())
                    .notes(event.getNotes())
                    .build();
        } else {
            // Update existing
            log.info("Updating existing previous consultation record - Consultation ID: {}", event.getConsultationId());
            entity.setStatus(event.getStatus());
            entity.setCompletedAt(event.getCompletedAt());
            if (event.getNotes() != null) {
                entity.setNotes(event.getNotes());
            }
        }

        entity = repository.save(entity);
        log.info("Previous consultation record saved - ID: {}, Consultation ID: {}", entity.getId(), entity.getConsultationId());

        return convertToDomain(entity);
    }

    private PreviousConsultation convertToDomain(PreviousConsultationEntity entity) {
        return PreviousConsultation.builder()
                .id(entity.getId())
                .consultationId(entity.getConsultationId())
                .patientId(entity.getPatientId())
                .patientName(entity.getPatientName())
                .patientEmail(entity.getPatientEmail())
                .doctorId(entity.getDoctorId())
                .doctorName(entity.getDoctorName())
                .doctorSpecialty(entity.getDoctorSpecialty())
                .status(entity.getStatus())
                .scheduledAt(entity.getScheduledAt())
                .completedAt(entity.getCompletedAt())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
