package com.github.notification_bot.bot;

import com.github.notification_bot.bot.service.FindNewArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// сканирование аннотации @Component в указанных директориях
@ComponentScan(basePackages = {
		"com.github.notification_bot.bot.bot",
		"org.telegram.telegrambots",
		"com.github.notification_bot.bot.service",
		"com.github.notification_bot.bot.repository",
		"com.github.notification_bot.bot.javarushclient",
		"com.github.notification_bot.bot.job",
		"com.github.notification_bot.bot.command"
})

@SpringBootApplication
@EnableScheduling
public class PetBotApplication {

	public static void main(String[] args) {

		// запуск приложения
		SpringApplication.run(PetBotApplication.class, args);
	}
}