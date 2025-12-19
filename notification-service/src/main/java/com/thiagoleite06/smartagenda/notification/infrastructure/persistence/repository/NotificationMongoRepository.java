package com.thiagoleite06.smartagenda.notification.infrastructure.persistence.repository;

import com.thiagoleite06.smartagenda.notification.infrastructure.persistence.document.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {

    List<NotificationDocument> findByRecipientOrderByCreatedAtDesc(String recipient);

    List<NotificationDocument> findByChannelOrderByCreatedAtDesc(String channel);

    List<NotificationDocument> findByStatus(String status);
}
