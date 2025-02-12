package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.javarushclient.dto.GroupDiscussionInfo;
import com.github.notification_bot.bot.repository.entity.GroupSub;

public interface GroupSubService {
    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);
}
