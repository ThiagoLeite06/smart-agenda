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
public class CreateDoctorUseCase {

    private final DoctorJpaRepository doctorRepository;

    @Transactional
    public Doctor execute(Doctor doctor) {
        log.info("Creating doctor with email: {} and CRM: {}", doctor.getEmail(), doctor.getCrm());

        // Validate email uniqueness
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("Doctor with email " + doctor.getEmail() + " already exists");
        }

        // Validate CRM uniqueness
        if (doctorRepository.existsByCrm(doctor.getCrm())) {
            throw new IllegalArgumentException("Doctor with CRM " + doctor.getCrm() + " already exists");
        }

        // Convert domain to entity
        DoctorEntity entity = DoctorEntity.builder()
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialty(doctor.getSpecialty())
                .crm(doctor.getCrm())
                .build();

        // Save
        entity = doctorRepository.save(entity);

        log.info("Doctor created successfully with ID: {}", entity.getId());

        // Convert entity back to domain
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
