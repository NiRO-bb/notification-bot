package com.github.notification_bot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandUtils {

    public static Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    public static String getMessage(Update update) {
        return update.getMessage().getText();
    }
}
