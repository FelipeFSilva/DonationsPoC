package br.org.donations.authorizationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthorizationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApiApplication.class, args);
	}

}
