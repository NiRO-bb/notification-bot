package com.github.notification_bot.bot;

import com.github.notification_bot.bot.service.SendBotMessageService;
import com.github.notification_bot.bot.bot.NotificaitonBot;
import com.github.notification_bot.bot.service.SendBotMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@DisplayName("Unit-level testing for SendBotMessageService")
public class SendBotMessageServiceTest {

    private SendBotMessageService sendBotMessageService;
    private NotificaitonBot notificaitonBot;

    @BeforeEach
    public void init() {
        notificaitonBot = Mockito.mock(NotificaitonBot.class);
        sendBotMessageService = new SendBotMessageServiceImpl(notificaitonBot);
    }

    @Test
    public void shouldProperlySendMessage() throws TelegramApiException {
        String chatId = "test_chat_id";
        String message = "test_message";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);

        sendBotMessageService.sendMessage(chatId, message);

        Mockito.verify(notificaitonBot).execute(sendMessage);
    }
}