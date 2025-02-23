package com.github.notification_bot.bot.service;

import org.jvnet.hk2.annotations.Service;

@Service
public interface FindNewArticleService {
    void findNewArticles();
}
