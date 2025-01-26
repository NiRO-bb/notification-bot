package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.bot.NotificaitonBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final NotificaitonBot notificaitonBot;

    @Autowired
    public SendBotMessageServiceImpl(NotificaitonBot notificaitonBot) {
        this.notificaitonBot = notificaitonBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            notificaitonBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {

            e.printStackTrace();
        }
    }
}
