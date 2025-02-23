package com.github.notification_bot.bot.javarushclient;

import com.github.notification_bot.bot.javarushclient.dto.PostInfo;

import java.util.List;

public interface JavaRushPostClient {
    List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId);
}
