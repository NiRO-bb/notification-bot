package com.github.notification_bot.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
		"com.github.notification_bot.bot.bot",
		"org.telegram.telegrambots",
		"com.github.notification_bot.bot.service"
})

@SpringBootApplication
public class PetBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetBotApplication.class, args);
	}

}
