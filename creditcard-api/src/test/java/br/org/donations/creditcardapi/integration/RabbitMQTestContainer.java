package br.org.donations.creditcardapi.integration;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicBoolean;

public class RabbitMQTestContainer implements BeforeAllCallback {

    private static AtomicBoolean containerStarted = new AtomicBoolean(false);

    private final static GenericContainer rabbitMQ = new GenericContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExposedPorts(5672)
            .withEnv("RABBITMQ_DEFAULT_USER","user")
            .withEnv("RABBITMQ_DEFAULT_PASS","Rabbit123!");

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!containerStarted.get()) {
            rabbitMQ.start();
            System.setProperty("spring.rabbitmq.host", rabbitMQ.getHost());
            System.setProperty("spring.rabbitmq.port", rabbitMQ.getFirstMappedPort().toString());

            containerStarted.set(true);
        }
    }
}
