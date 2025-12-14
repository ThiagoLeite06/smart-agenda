package com.thiagoleite06.smartagenda.schedule.usecase.patient;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
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
public class GetAllPatientsUseCase {

    private final PatientJpaRepository patientRepository;

    @Transactional(readOnly = true)
    public List<Patient> execute() {
        log.info("Getting all patients");

        return patientRepository.findAll().stream()
                .map(entity -> Patient.builder()
                        .id(entity.getId())
                        .fullName(entity.getFullName())
                        .email(entity.getEmail())
                        .phone(entity.getPhone())
                        .dateOfBirth(entity.getDateOfBirth())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
