package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.javarushclient.JavaRushPostClient;
import com.github.notification_bot.bot.javarushclient.dto.PostInfo;
import com.github.notification_bot.bot.repository.entity.GroupSub;
import com.github.notification_bot.bot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewArticleServiceImpl implements FindNewArticleService {
    public static final String JAVARUSH_WEB_POST_FORMAT = "https://javarush.com/groups/posts/%s";

    private final GroupSubService groupSubService;
    private final JavaRushPostClient javaRushPostClient;
    private final SendBotMessageService sendMessageService;

    @Autowired
    public FindNewArticleServiceImpl(GroupSubService groupSubService,
                                     JavaRushPostClient javaRushPostClient,
                                     SendBotMessageService sendMessageService) {
        this.groupSubService = groupSubService;
        this.javaRushPostClient = javaRushPostClient;
        this.sendMessageService = sendMessageService;
    }


    @Override
    public void findNewArticles() {
        // найти новые статьи для каждой группы из group_sub
        groupSubService.findAll().forEach(group ->
        {
            // получить новые статьи
            List<PostInfo> newPosts = javaRushPostClient.findNewPosts(group.getId(), group.getLastArticleId());

            // сохранить номер статьи в last_article_id
            setNewLastArticleId(group, newPosts);

            // отправить подписчикам сообщения о выходе новых статей
            notifySubscribersAboutNewArticles(group, newPosts);
        });
    }

    private void notifySubscribersAboutNewArticles(GroupSub gSub, List<PostInfo> newPosts) {
        // сообщить о выходе статей в обратном порядке (от старых к новым)
        Collections.reverse(newPosts);

        // создать сообщения
        List<String> messagesWithNewArticles = newPosts.stream()
                .map(post -> String.format("Вышла новая статья %s в группе %s.\n\n" +
                                "Описание: %s\n\n" +
                                "Ссылка: %s\n",
                        post.getTitle(), gSub.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .collect(Collectors.toList());

        // отправить непустые сообщения активным пользователям
        if (!messagesWithNewArticles.isEmpty()) {
            gSub.getUsers().stream()
                    .filter(TelegramUser::isActive)
                    .forEach(user -> sendMessageService.sendMessage(user.getChatId(), messagesWithNewArticles.toString()));
        }
    }

    private void setNewLastArticleId(GroupSub gSub, List<PostInfo> newPosts) {
        newPosts.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(id -> {
                    // сохранить номер статьи в last_article_id
                    gSub.setLastArticleId(id);

                    // обновить таблицу group_sub
                    groupSubService.save(gSub);
                });
    }

    private String getPostUrl(String key) {
        return String.format(JAVARUSH_WEB_POST_FORMAT, key);
    }
}
