package com.Ecommerce.tubes_PBO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TubesPboApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubesPboApplication.class, args);
	}

}
