package com.github.notification_bot.bot.command;

import com.github.notification_bot.bot.service.SendBotMessageService;
import com.github.notification_bot.bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public static final String STOP_COMMAND = "Все уведомления отключены.";

    public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), STOP_COMMAND);
        telegramUserService.findByChatId(update.getMessage().getChatId().toString()).ifPresent(it -> {
           it.setActive(false);
           telegramUserService.save(it);
        });
    }
}
