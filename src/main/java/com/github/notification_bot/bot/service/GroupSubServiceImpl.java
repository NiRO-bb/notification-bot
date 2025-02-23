package com.github.notification_bot.bot.service;

import com.github.notification_bot.bot.javarushclient.JavaRushGroupClient;
import com.github.notification_bot.bot.javarushclient.dto.GroupDiscussionInfo;
import com.github.notification_bot.bot.repository.GroupSubRepository;
import com.github.notification_bot.bot.repository.entity.GroupSub;
import com.github.notification_bot.bot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class GroupSubServiceImpl implements GroupSubService {

    private final GroupSubRepository groupSubRepository;
    private final TelegramUserService telegramUserService;
    private final JavaRushGroupClient javaRushGroupClient;

    @Autowired
    public GroupSubServiceImpl(GroupSubRepository groupSubRepository, TelegramUserService telegramUserService, JavaRushGroupClient javaRushGroupClient) {
        this.groupSubRepository = groupSubRepository;
        this.telegramUserService = telegramUserService;
        this.javaRushGroupClient = javaRushGroupClient;
    }

    @Override
    public GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo) {
        // получить пользователя по chatId
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);

        GroupSub groupSub;

        // получить группу по id
        Optional<GroupSub> groupSubFromDB = groupSubRepository.findById(groupDiscussionInfo.getId());

        if(groupSubFromDB.isPresent()) {
            groupSub = groupSubFromDB.get();

            // найти в списке пользователей пользователя с нужым chatId
            Optional<TelegramUser> first = groupSub.getUsers().stream()
                    .filter(user -> user.getChatId().equalsIgnoreCase(chatId))
                    .findFirst();

            if(first.isEmpty()) {
                // добавить пользователя в GroupSub
                groupSub.addUser(telegramUser);
            }
        } else {
            groupSub = new GroupSub();
            groupSub.addUser(telegramUser);
            groupSub.setLastArticleId(javaRushGroupClient.findLastPostId(groupDiscussionInfo.getId()));
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.setTitle(groupDiscussionInfo.getTitle());
        }

        return groupSubRepository.save(groupSub);
    }

    @Override
    public GroupSub save(GroupSub groupSub) {
        return groupSubRepository.save(groupSub);
    }

    @Override
    public Optional<GroupSub> findById(Integer id) {
        return groupSubRepository.findById(id);
    }

    @Override
    public List<GroupSub> findAll() {
        return groupSubRepository.findAll();
    }
}