package com.github.notification_bot.bot.command;

import com.github.notification_bot.bot.javarushclient.JavaRushGroupClient;
import com.github.notification_bot.bot.javarushclient.dto.GroupDiscussionInfo;
import com.github.notification_bot.bot.javarushclient.dto.GroupRequestArgs;
import com.github.notification_bot.bot.repository.entity.GroupSub;
import com.github.notification_bot.bot.service.GroupSubService;
import com.github.notification_bot.bot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.notification_bot.bot.command.CommandName.ADD_GROUP_SUB;
import static com.github.notification_bot.bot.command.CommandUtils.getChatId;
import static com.github.notification_bot.bot.command.CommandUtils.getMessage;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class AddGroupSubCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final JavaRushGroupClient javaRushGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JavaRushGroupClient javaRushGroupClient,
                              GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.javaRushGroupClient = javaRushGroupClient;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        /// команда без номера группы
        /// '/addgroupsub'
        if (getMessage(update).equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())) {
            sendGroupIdList(String.valueOf(getChatId(update)));
            return;
        }

        /// полноценная команда (с номером группы)
        /// '/addgroupsub 1'
        // получить номер выбранной группы
        String groupId = getMessage(update).split(SPACE)[1];
        String chatId = String.valueOf(getChatId(update));
        // проверка на наличие числа
        if (isNumeric(groupId)) {

            // получить группу с заданным ID
            GroupDiscussionInfo groupById = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
            if (isNull(groupById.getId())) {
                sendGroupNotFound(chatId, groupId);
            }

            // сохранить уникальную пару 'подписчик - группа'
            GroupSub savedGroupSub = groupSubService.save(chatId, groupById);
            sendBotMessageService.sendMessage(chatId, "Подписал на группу " + savedGroupSub.getTitle());
        } else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        String groupNotFoundMessage = "Нет группы с ID = \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(groupNotFoundMessage, groupId));
    }

    private void sendGroupIdList(String chatId) {
        String groupIds = javaRushGroupClient.getGroupList(GroupRequestArgs.builder().build()).stream()
                .map(group -> String.format("%s - %s \n", group.getTitle(), group.getId()))
                .collect(Collectors.joining());

        String message = "Чтобы подписаться на группу - передай команду вместе с ID группы. \n" +
                "Например: /addGroupSub 16. \n\n" +
                "Вот список групп\n\n" +
                "(имя группы - ID группы):\n\n" +
                "%s";

        sendBotMessageService.sendMessage(chatId, String.format(message, groupIds));
    }
}