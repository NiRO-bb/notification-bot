package com.github.notification_bot.bot.command;

import com.github.notification_bot.bot.repository.entity.TelegramUser;
import com.github.notification_bot.bot.service.SendBotMessageService;
import com.github.notification_bot.bot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import jakarta.ws.rs.NotFoundException;
import java.util.stream.Collectors;

import static com.github.notification_bot.bot.command.CommandUtils.getChatId;

public class ListGroupSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public ListGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {

        TelegramUser telegramUser = telegramUserService.findByChatId(String.valueOf(getChatId(update)))
                .orElseThrow(NotFoundException::new);

        String message = "Я нашел все подписки на группы: \n\n";
        String collectedGroups = telegramUser.getGroupSubs().stream()
                .map(it -> "Группа: " + it.getTitle() + " , ID = " + it.getId() + " \n")
                .collect(Collectors.joining());

        sendBotMessageService.sendMessage(telegramUser.getChatId(), message + collectedGroups);
    }
}