package com.thiagoleite06.smartagenda.schedule.usecase.consultation;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Consultation;
import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
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
public class CreateConsultationUseCase {

    private final ConsultationJpaRepository consultationRepository;
    private final PatientJpaRepository patientRepository;
    private final DoctorJpaRepository doctorRepository;

    @Transactional
    public Consultation execute(Consultation consultation) {
        log.info("Creating consultation for patient ID: {} with doctor ID: {} at {}",
                consultation.getPatientId(), consultation.getDoctorId(), consultation.getScheduledAt());

        // Validate patient exists
        if (!patientRepository.existsById(consultation.getPatientId())) {
            throw new IllegalArgumentException("Patient with ID " + consultation.getPatientId() + " not found");
        }

        // Validate doctor exists
        if (!doctorRepository.existsById(consultation.getDoctorId())) {
            throw new IllegalArgumentException("Doctor with ID " + consultation.getDoctorId() + " not found");
        }

        // Validate scheduled time is in the future
        if (consultation.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future");
        }

        // Convert domain to entity
        ConsultationEntity entity = ConsultationEntity.builder()
                .patientId(consultation.getPatientId())
                .doctorId(consultation.getDoctorId())
                .status(consultation.getStatus() != null ? consultation.getStatus() : ConsultationStatus.SCHEDULED)
                .scheduledAt(consultation.getScheduledAt())
                .notes(consultation.getNotes())
                .build();

        // Save
        entity = consultationRepository.save(entity);

        log.info("Consultation created successfully with ID: {}", entity.getId());

        // Fetch patient and doctor details
        var patientEntity = patientRepository.findById(entity.getPatientId()).orElse(null);
        var doctorEntity = doctorRepository.findById(entity.getDoctorId()).orElse(null);

        // Convert entity back to domain
        return Consultation.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .doctorId(entity.getDoctorId())
                .patient(patientEntity != null ? com.thiagoleite06.smartagenda.schedule.domain.entity.Patient.builder()
                        .id(patientEntity.getId())
                        .fullName(patientEntity.getFullName())
                        .email(patientEntity.getEmail())
                        .phone(patientEntity.getPhone())
                        .dateOfBirth(patientEntity.getDateOfBirth())
                        .createdAt(patientEntity.getCreatedAt())
                        .updatedAt(patientEntity.getUpdatedAt())
                        .build() : null)
                .doctor(doctorEntity != null ? com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor.builder()
                        .id(doctorEntity.getId())
                        .fullName(doctorEntity.getFullName())
                        .email(doctorEntity.getEmail())
                        .phone(doctorEntity.getPhone())
                        .specialty(doctorEntity.getSpecialty())
                        .crm(doctorEntity.getCrm())
                        .createdAt(doctorEntity.getCreatedAt())
                        .updatedAt(doctorEntity.getUpdatedAt())
                        .build() : null)
                .status(entity.getStatus())
                .scheduledAt(entity.getScheduledAt())
                .completedAt(entity.getCompletedAt())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
