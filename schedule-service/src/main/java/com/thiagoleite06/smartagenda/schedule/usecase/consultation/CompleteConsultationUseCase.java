package com.thiagoleite06.smartagenda.schedule.usecase.consultation;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Consultation;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event.ConsultationCompletedEvent;
import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event.NotificationRequest;
import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.publisher.HistoryEventPublisher;
import com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.publisher.NotificationEventPublisher;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.ConsultationEntity;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.ConsultationJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.DoctorJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteConsultationUseCase {

    private final ConsultationJpaRepository consultationRepository;
    private final PatientJpaRepository patientRepository;
    private final DoctorJpaRepository doctorRepository;
    private final HistoryEventPublisher historyEventPublisher;
    private final NotificationEventPublisher notificationEventPublisher;

    @Transactional
    public Consultation execute(Long id) {
        log.info("Completing consultation with ID: {}", id);

        ConsultationEntity entity = consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultation with ID " + id + " not found"));

        entity.setStatus(ConsultationStatus.ACCOMPLISHED);
        entity.setCompletedAt(LocalDateTime.now());
        entity = consultationRepository.save(entity);

        log.info("Consultation completed successfully with ID: {}", entity.getId());

        // Publish events
        var patientEntity = patientRepository.findById(entity.getPatientId()).orElse(null);
        var doctorEntity = doctorRepository.findById(entity.getDoctorId()).orElse(null);

        if (patientEntity != null && doctorEntity != null) {
            // Publish to history service
            ConsultationCompletedEvent historyEvent = ConsultationCompletedEvent.builder()
                    .consultationId(entity.getId())
                    .patientId(entity.getPatientId())
                    .patientName(patientEntity.getFullName())
                    .patientEmail(patientEntity.getEmail())
                    .doctorId(entity.getDoctorId())
                    .doctorName(doctorEntity.getFullName())
                    .doctorSpecialty(doctorEntity.getSpecialty().name())
                    .status(entity.getStatus())
                    .scheduledAt(entity.getScheduledAt())
                    .completedAt(entity.getCompletedAt())
                    .notes(entity.getNotes())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
            historyEventPublisher.publishConsultationUpdate(historyEvent);

            // Publish notification
            NotificationRequest notification = NotificationRequest.builder()
                    .recipient(patientEntity.getEmail())
                    .channel("EMAIL")
                    .subject("Consultation Completed")
                    .message(String.format("Your consultation with Dr. %s has been completed on %s",
                            doctorEntity.getFullName(),
                            entity.getCompletedAt()))
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationEventPublisher.publishNotification(notification);
        }

        return convertToConsultation(entity);
    }

    private Consultation convertToConsultation(ConsultationEntity entity) {
        Patient patient = patientRepository.findById(entity.getPatientId())
                .map(p -> Patient.builder()
                        .id(p.getId())
                        .fullName(p.getFullName())
                        .email(p.getEmail())
                        .phone(p.getPhone())
                        .dateOfBirth(p.getDateOfBirth())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .orElse(null);

        Doctor doctor = doctorRepository.findById(entity.getDoctorId())
                .map(d -> Doctor.builder()
                        .id(d.getId())
                        .fullName(d.getFullName())
                        .email(d.getEmail())
                        .phone(d.getPhone())
                        .specialty(d.getSpecialty())
                        .crm(d.getCrm())
                        .createdAt(d.getCreatedAt())
                        .updatedAt(d.getUpdatedAt())
                        .build())
                .orElse(null);

        return Consultation.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .doctorId(entity.getDoctorId())
                .patient(patient)
                .doctor(doctor)
                .status(entity.getStatus())
                .scheduledAt(entity.getScheduledAt())
                .completedAt(entity.getCompletedAt())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
