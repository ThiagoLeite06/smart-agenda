package com.thiagoleite06.smartagenda.history.usecase;

import com.thiagoleite06.smartagenda.history.domain.entity.PreviousConsultation;
import com.thiagoleite06.smartagenda.history.infrastructure.persistence.repository.PreviousConsultationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetConsultationHistoryByPatientIdUseCase {

    private final PreviousConsultationJpaRepository repository;

    @Transactional(readOnly = true)
    public List<PreviousConsultation> execute(Long patientId) {
        log.info("Getting consultation history for patient ID: {}", patientId);

        return repository.findByPatientIdOrderByScheduledAtDesc(patientId).stream()
                .map(entity -> PreviousConsultation.builder()
                        .id(entity.getId())
                        .consultationId(entity.getConsultationId())
                        .patientId(entity.getPatientId())
                        .patientName(entity.getPatientName())
                        .patientEmail(entity.getPatientEmail())
                        .doctorId(entity.getDoctorId())
                        .doctorName(entity.getDoctorName())
                        .doctorSpecialty(entity.getDoctorSpecialty())
                        .status(entity.getStatus())
                        .scheduledAt(entity.getScheduledAt())
                        .completedAt(entity.getCompletedAt())
                        .notes(entity.getNotes())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
