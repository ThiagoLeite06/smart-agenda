package com.thiagoleite06.smartagenda.schedule.usecase.consultation;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Consultation;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.ConsultationEntity;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.ConsultationJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.DoctorJpaRepository;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetConsultationsByDoctorIdUseCase {

    private final ConsultationJpaRepository consultationRepository;
    private final PatientJpaRepository patientRepository;
    private final DoctorJpaRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<Consultation> execute(Long doctorId) {
        log.info("Getting consultations for doctor ID: {}", doctorId);

        return consultationRepository.findByDoctorIdOrderByScheduledAtDesc(doctorId).stream()
                .map(this::convertToConsultation)
                .collect(Collectors.toList());
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
