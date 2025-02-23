package com.github.notification_bot.bot.repository;

import com.github.notification_bot.bot.repository.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
    // найти все поля таблицы по значению 'true' поля 'active'
    List<TelegramUser> findAllByActiveTrue();
}