package com.thiagoleite06.smartagenda.schedule.usecase.doctor;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.DoctorEntity;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository.DoctorJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDoctorByIdUseCase {

    private final DoctorJpaRepository doctorRepository;

    @Transactional(readOnly = true)
    public Doctor execute(Long id) {
        log.info("Getting doctor with ID: {}", id);

        DoctorEntity entity = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor with ID " + id + " not found"));

        return Doctor.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .specialty(entity.getSpecialty())
                .crm(entity.getCrm())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
