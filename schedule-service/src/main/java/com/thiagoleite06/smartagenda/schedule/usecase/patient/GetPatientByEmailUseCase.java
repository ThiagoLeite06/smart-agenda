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
public class GetPatientByEmailUseCase {

    private final PatientJpaRepository patientRepository;

    @Transactional(readOnly = true)
    public Patient execute(String email) {
        log.info("Getting patient with email: {}", email);

        PatientEntity entity = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with email " + email + " not found"));

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
