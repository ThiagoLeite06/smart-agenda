package com.thiagoleite06.smartagenda.notification.infrastructure.graphql.resolver;

import com.thiagoleite06.smartagenda.notification.domain.entity.Notification;
import com.thiagoleite06.smartagenda.notification.usecase.GetNotificationsByRecipientUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationQueryResolver {

    private final GetNotificationsByRecipientUseCase getNotificationsUseCase;

    @QueryMapping
    public List<Notification> getNotificationsByRecipient(@Argument String recipient) {
        log.info("GraphQL Query: getNotificationsByRecipient({})", recipient);
        return getNotificationsUseCase.execute(recipient);
    }
}
