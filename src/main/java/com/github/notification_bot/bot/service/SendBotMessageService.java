package com.github.notification_bot.bot.service;

public interface SendBotMessageService {

    void sendMessage(String chatId, String message);
}