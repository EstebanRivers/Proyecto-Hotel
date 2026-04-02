package com.proyecto.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CommonsReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonsReservasApplication.class, args);
	}

}
