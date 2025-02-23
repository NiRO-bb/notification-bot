package com.github.notification_bot.bot.bot;

import com.github.notification_bot.bot.command.CommandContainer;
import com.github.notification_bot.bot.javarushclient.JavaRushGroupClient;
import com.github.notification_bot.bot.service.GroupSubService;
import com.github.notification_bot.bot.service.SendBotMessageServiceImpl;
import com.github.notification_bot.bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.notification_bot.bot.command.CommandName.NO;

@Component
public class NotificationBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    // подставляем значения из файла application.properties
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public NotificationBot(TelegramUserService telegramUserService, JavaRushGroupClient groupClient, GroupSubService groupSubService) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService, groupClient, groupSubService);
    }

    // "реакция" бота на сообщения пользователя
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {

            // удалить пробелы с начала и конца сообщения
            String message = update.getMessage().getText().trim();

            // если сообщение является командой
            if (message.startsWith(COMMAND_PREFIX)) {
                // получить первое слово из сообщения (команду)
                String commandIdentifier = message.split(" ")[0].toLowerCase();

                // найти соответствующую команду и выполнить ее
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }
    }

    // имя и токен бота
    @Override
    public String getBotUsername() {
        return username;
    }
    @Override
    public String getBotToken() {
        return token;
    }
}
