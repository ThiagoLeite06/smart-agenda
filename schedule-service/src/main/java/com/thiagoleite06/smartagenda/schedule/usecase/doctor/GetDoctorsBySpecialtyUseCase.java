package com.thiagoleite06.smartagenda.schedule.usecase.doctor;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.domain.enums.Specialty;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.DoctorJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDoctorsBySpecialtyUseCase {

    private final DoctorJpaRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<Doctor> execute(Specialty specialty) {
        log.info("Getting doctors with specialty: {}", specialty);

        return doctorRepository.findBySpecialty(specialty).stream()
                .map(entity -> Doctor.builder()
                        .id(entity.getId())
                        .fullName(entity.getFullName())
                        .email(entity.getEmail())
                        .phone(entity.getPhone())
                        .specialty(entity.getSpecialty())
                        .crm(entity.getCrm())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
