package com.github.notification_bot.bot.javarushclient;

import com.github.notification_bot.bot.javarushclient.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JavaRushPostClientImpl implements JavaRushPostClient {
    private final String javarushApiPostPath;

    public JavaRushPostClientImpl(@Value("${javarush.api.path}") String javarushApi) {
        this.javarushApiPostPath = javarushApi + "/posts";
    }

    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        // получить список статей через GET запрос
        List<PostInfo> lastPosts = Unirest.get("https://javarush.com/api/1.0/rest/posts?groupKid=%s&order=NEW&limit=5".formatted(groupId))
                .asObject(new GenericType<List<PostInfo>>() {})
                .getBody();

        List<PostInfo> newPosts = new ArrayList<>();

        for (PostInfo post : lastPosts) {
            // сравнить номера новой статьи и последней сохраненной статьи
            if (lastPostId.equals(post.getId())) {
                // завершить добавление новых статей при нахождении номера сохраненной статьи
                return newPosts;
            }

            // сохранить новую статью
            newPosts.add(post);
        }
        return newPosts;
    }
}