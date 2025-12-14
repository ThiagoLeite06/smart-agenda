package com.thiagoleite06.smartagenda.schedule.domain.entity;

import com.thiagoleite06.smartagenda.schedule.domain.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Specialty specialty;
    private String crm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
