package com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event;

import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreatedEvent implements Serializable {

    private Long consultationId;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialty;
    private ConsultationStatus status;
    private LocalDateTime scheduledAt;
    private String notes;
    private LocalDateTime createdAt;
}
