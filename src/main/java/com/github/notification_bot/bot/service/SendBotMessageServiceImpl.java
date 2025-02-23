package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.bot.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final NotificationBot notificationBot;

    @Autowired
    public SendBotMessageServiceImpl(NotificationBot notificationBot) {
        this.notificationBot = notificationBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.enableHtml(true);

        try {
            notificationBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {

            e.printStackTrace();
        }
    }
}
