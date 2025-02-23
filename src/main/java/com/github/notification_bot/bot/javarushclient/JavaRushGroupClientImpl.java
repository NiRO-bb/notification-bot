package com.github.notification_bot.bot.javarushclient;

import com.github.notification_bot.bot.javarushclient.dto.*;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class JavaRushGroupClientImpl implements JavaRushGroupClient {

    private final String javarushApiGroupPath;

    public JavaRushGroupClientImpl(@Value("${javarush.api.path}") String javarushApi) {
        this.javarushApiGroupPath = javarushApi + "/groups";
    }

    @Override
    public List<GroupInfo> getGroupList(GroupRequestArgs requestArgs) {
        return Unirest.get(javarushApiGroupPath)// сформировать GET запрос
                .queryString(requestArgs.populateQueries()) // указать параметры (аргументы) запроса
                .asObject(new GenericType<List<GroupInfo>>() {}) // получить ответ в виде структуры данных, переданной в качестве аргумента
                .getBody(); // получить "тело" запроса
    }

    @Override
    public List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs) {
        return Unirest.get(javarushApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupDiscussionInfo>>() {})
                .getBody();
    }

    @Override
    public Integer getGroupCount(GroupsCountRequestArgs countRequestArgs) {
        return Integer.valueOf(
                Unirest.get(String.format("%s/count", javarushApiGroupPath))
                        .queryString(countRequestArgs.populateQueries())
                        .asString()
                        .getBody()
        );
    }

    @Override
    public GroupDiscussionInfo getGroupById(Integer id) {
        return Unirest.get(String.format("%s/group%s", javarushApiGroupPath, id.toString()))
                .asObject(GroupDiscussionInfo.class)
                .getBody();
    }

    @Override
    public Integer findLastPostId(Integer groupId) {
        List<PostInfo> posts = Unirest.get("https://javarush.com/api/1.0/rest/posts?groupKid=%s&order=NEW&limit=1".formatted(groupId))
                .asObject(new GenericType<List<PostInfo>>() {})
                .getBody();

        return isEmpty(posts) ? 0 : Optional.ofNullable(posts.get(0)).map(PostInfo::getId).orElse(0);
    }
}