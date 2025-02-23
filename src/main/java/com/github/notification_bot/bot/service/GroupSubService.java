package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.javarushclient.dto.GroupDiscussionInfo;
import com.github.notification_bot.bot.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;

public interface GroupSubService {
    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);

    GroupSub save(GroupSub groupSub);

    Optional<GroupSub> findById(Integer id);

    List<GroupSub> findAll();
}
