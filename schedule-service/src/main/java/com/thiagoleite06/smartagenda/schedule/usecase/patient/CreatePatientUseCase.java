package com.thiagoleite06.smartagenda.schedule.usecase.patient;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.PatientEntity;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePatientUseCase {

    private final PatientJpaRepository patientRepository;

    @Transactional
    public Patient execute(Patient patient) {
        log.info("Creating patient with email: {}", patient.getEmail());

        // Validate email uniqueness
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Patient with email " + patient.getEmail() + " already exists");
        }

        // Convert domain to entity
        PatientEntity entity = PatientEntity.builder()
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .build();

        // Save
        entity = patientRepository.save(entity);

        log.info("Patient created successfully with ID: {}", entity.getId());

        // Convert entity back to domain
        return Patient.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dateOfBirth(entity.getDateOfBirth())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
