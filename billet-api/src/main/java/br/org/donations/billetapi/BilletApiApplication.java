package br.org.donations.billetapi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@EnableFeignClients
@SpringBootApplication
public class BilletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilletApiApplication.class, args);
	}

}
