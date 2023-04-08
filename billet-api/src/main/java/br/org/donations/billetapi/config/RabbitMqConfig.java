package br.org.donations.billetapi.config;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMqConfig {

    @Value("${app-config.rabbit.exchange.billet}")
    private String billetTopicExchange;

    @Value("${app-config.rabbit.routingKey.billet}")
    private String billetKey;

    @Value("${app-config.rabbit.queue.billet}")
    private String billetQueue;

    @Value("${app-config.rabbit.routingKey.billet-retry}")
    private String billetRetryKey;

    @Value("${app-config.rabbit.queue.billet-retry}")
    private String billetRetryQueue;

    @Value("${app-config.rabbit.routingKey.billet-dlq}")
    private String billetKeyDLQ;

    @Value("${app-config.rabbit.queue.billet-dlq}")
    private String billetQueueDLQ;

    @Value("${app-config.rabbit.retry-policy.initial-interval}")
    private long initialInterval;
    @Value("${app-config.rabbit.retry-policy.max-interval}")
    private long maxInterval;

    @Bean
    public TopicExchange billetTopicExchange(){
        return new TopicExchange(billetTopicExchange);
    }

    @Bean
    public Queue billetQueue(){
        return createQueue(billetQueue, 0, billetTopicExchange, billetRetryKey);
    }

    @Bean
    public Queue billetQueueDLQ(){
        return new Queue(billetQueueDLQ, true);
    }

    @Bean
    public Queue billetRetryQueue(){
        return createQueue(billetRetryQueue, 0, billetTopicExchange, billetKeyDLQ);
    }

    private Queue createQueue(String queueName, int deliveryLimit, String deadLetterExchange, String deadLetterRountingKey){
        return QueueBuilder
                .durable(queueName)
                .quorum()
                .deliveryLimit(deliveryLimit)
                .deadLetterExchange(deadLetterExchange)
                .deadLetterRoutingKey(deadLetterRountingKey)
                .build();
    }

    @Bean
    public RetryTemplate retryTemplate(){
        return RetryTemplate
                .builder()
                .notRetryOn(ConstraintViolationException.class)
                .notRetryOn(FeignException.UnprocessableEntity.class)
                .maxAttempts(3)
                .exponentialBackoff(initialInterval, 2, maxInterval)
                .build();
    }

    @Bean
    public Binding billetBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(billetQueue())
                .to(topicExchange)
                .with(billetKey);
    }

    @Bean
    public Binding billetBindingDLQ(TopicExchange topicExchange){
        return BindingBuilder
                .bind(billetQueueDLQ())
                .to(topicExchange)
                .with(billetKeyDLQ);
    }

    @Bean
    public Binding billetRetryBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(billetRetryQueue())
                .to(topicExchange)
                .with(billetRetryKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
