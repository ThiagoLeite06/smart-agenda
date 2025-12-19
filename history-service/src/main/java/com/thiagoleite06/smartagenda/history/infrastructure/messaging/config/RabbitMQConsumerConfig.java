package com.thiagoleite06.smartagenda.history.infrastructure.messaging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    public static final String HISTORY_NEW_QUEUE = "history_new_queue";
    public static final String HISTORY_UPDATE_QUEUE = "history_update_queue";

    @Bean
    public Queue historyNewQueue() {
        return new Queue(HISTORY_NEW_QUEUE, true);
    }

    @Bean
    public Queue historyUpdateQueue() {
        return new Queue(HISTORY_UPDATE_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
