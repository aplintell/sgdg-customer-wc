package com.pns.sgdg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:app-config.xml")
@ComponentScan("com.pns.sgdg.*")
public class SgdgApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgdgApplication.class, args);
	}
}
