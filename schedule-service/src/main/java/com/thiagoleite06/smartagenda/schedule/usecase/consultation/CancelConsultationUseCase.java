package com.thiagoleite06.smartagenda.schedule.usecase.consultation;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Consultation;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.ConsultationEntity;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.ConsultationJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.DoctorJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelConsultationUseCase {

    private final ConsultationJpaRepository consultationRepository;
    private final PatientJpaRepository patientRepository;
    private final DoctorJpaRepository doctorRepository;

    @Transactional
    public Consultation execute(Long id) {
        log.info("Cancelling consultation with ID: {}", id);

        ConsultationEntity entity = consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultation with ID " + id + " not found"));

        entity.setStatus(ConsultationStatus.CANCELLED);
        entity = consultationRepository.save(entity);

        log.info("Consultation cancelled successfully with ID: {}", entity.getId());

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
