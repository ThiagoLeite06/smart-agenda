package com.thiagoleite06.smartagenda.schedule.infrastructure.messaging.event;

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
public class NotificationRequest implements Serializable {

    private String recipient;
    private String channel; // EMAIL, SMS, WHATSAPP
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}
