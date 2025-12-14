package com.thiagoleite06.smartagenda.schedule.domain.entity;

import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private ConsultationStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For queries with joined data
    private Patient patient;
    private Doctor doctor;
}
