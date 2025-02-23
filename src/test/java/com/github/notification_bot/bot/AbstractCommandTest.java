package com.github.notification_bot.bot;

import com.github.notification_bot.bot.bot.NotificationBot;
import com.github.notification_bot.bot.command.Command;
import com.github.notification_bot.bot.service.SendBotMessageService;
import com.github.notification_bot.bot.service.SendBotMessageServiceImpl;
import com.github.notification_bot.bot.service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommandTest {

    protected NotificationBot notificaitonBot = Mockito.mock(NotificationBot.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(notificaitonBot);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {

        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);


        getCommand().execute(update);


        Mockito.verify(notificaitonBot).execute(sendMessage);
    }
}