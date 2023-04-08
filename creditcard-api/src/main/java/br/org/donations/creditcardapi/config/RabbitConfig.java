package br.org.donations.creditcardapi.config;

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
public class RabbitConfig {

    @Value("${app-config.rabbit.exchange.credit-card}")
    private String creditCardTopicExchange;

    @Value("${app-config.rabbit.routingKey.credit-card}")
    private String creditCardKey;
    @Value("${app-config.rabbit.queue.credit-card}")
    private String creditCardQueue;

    @Value("${app-config.rabbit.routingKey.credit-card-retry}")
    private String creditCardRetryKey;
    @Value("${app-config.rabbit.queue.credit-card-retry}")
    private String creditCardRetryQueue;

    @Value("${app-config.rabbit.routingKey.credit-card-dlq}")
    private String creditCardKeyDLQ;
    @Value("${app-config.rabbit.queue.credit-card-dlq}")
    private String creditCardQueueDLQ;

    @Value("${app-config.rabbit.routingKey.credit-card-email}")
    private String creditCardEmailKey;
    @Value("${app-config.rabbit.queue.credit-card-email}")
    private String creditCardEmailQueue;

    @Value("${app-config.rabbit.retry-policy.initial-interval}")
    private long initialInterval;
    @Value("${app-config.rabbit.retry-policy.max-interval}")
    private long maxInterval;

    @Bean
    public TopicExchange creditCardTopicExchange(){
        return new TopicExchange(creditCardTopicExchange);
    }

    @Bean
    public Queue creditCardQueue(){
        return createQueue(creditCardQueue, 0, creditCardTopicExchange, creditCardRetryKey);
    }

    @Bean
    public Queue creditCardQueueDLQ(){
        return new Queue(creditCardQueueDLQ, true);
    }

    @Bean
    public Queue creditCardRetryQueue(){
        return createQueue(creditCardRetryQueue, 0, creditCardTopicExchange, creditCardKeyDLQ);
    }

    @Bean
    public Queue creditCardEmailQueue(){
        return new Queue(creditCardEmailQueue, true);
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
    public Binding creditCardBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(creditCardQueue())
                .to(topicExchange)
                .with(creditCardKey);
    }

    @Bean
    public Binding creditCardRetryBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(creditCardRetryQueue())
                .to(topicExchange)
                .with(creditCardRetryKey);
    }

    @Bean
    public Binding creditCardBindingDLQ(TopicExchange topicExchange){
        return BindingBuilder
                .bind(creditCardQueueDLQ())
                .to(topicExchange)
                .with(creditCardKeyDLQ);
    }

    @Bean
    public Binding creditCardEmailBinding(TopicExchange topicExchange){
        return BindingBuilder
                .bind(creditCardEmailQueue())
                .to(topicExchange)
                .with(creditCardEmailKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}