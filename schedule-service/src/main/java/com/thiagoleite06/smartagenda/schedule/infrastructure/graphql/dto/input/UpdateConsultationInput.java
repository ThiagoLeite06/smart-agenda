package com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.dto.input;

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
public class UpdateConsultationInput {

    private ConsultationStatus status;

    private LocalDateTime scheduledAt;

    private LocalDateTime completedAt;

    private String notes;
}
