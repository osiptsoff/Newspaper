package ru.spb.nicetu.newspaper.api;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * <p>Application's main class</p>
 * 
	* @author Nikita Osiptsov
 * @since 1.0
 */
@SpringBootApplication
@Slf4j
public class App {
	@Bean
	public Logger logger() {
		return log;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}