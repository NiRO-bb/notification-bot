package com.github.notification_bot.bot.command;

import com.github.notification_bot.bot.repository.entity.GroupSub;
import com.github.notification_bot.bot.repository.entity.TelegramUser;
import com.github.notification_bot.bot.service.GroupSubService;
import com.github.notification_bot.bot.service.SendBotMessageService;
import com.github.notification_bot.bot.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.notification_bot.bot.command.CommandName.DELETE_GROUP_SUB;
import static com.github.notification_bot.bot.command.CommandUtils.getChatId;
import static com.github.notification_bot.bot.command.CommandUtils.getMessage;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DeleteGroupSubCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final GroupSubService groupSubService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        /// команда без номера группы
        /// '/deletegroupsub'
        if (getMessage(update).equalsIgnoreCase(DELETE_GROUP_SUB.getCommandName())) {
            sendGroupIdList(String.valueOf(getChatId(update)));
            return;
        }

        /// полноценная команда (с номером группы)
        /// '/deletegroupsub 1'
        String groupId = getMessage(update).split(SPACE)[1];
        String chatId = String.valueOf(getChatId(update));
        if (isNumeric(groupId)) {
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.valueOf(groupId));
            if (optionalGroupSub.isPresent()) {
                GroupSub groupSub = optionalGroupSub.get();

                // получить пользователя по ID
                TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);

                // удалить пользователя из списка в GroupSub (Spring Data удалит этого же пользователя из связанных таблица tg_user и group_x_user)
                groupSub.getUsers().remove(telegramUser);

                // обновить сущность GroupSub (таблица group_sub)
                groupSubService.save(groupSub);
                sendBotMessageService.sendMessage(chatId, format("Удалил подписку на группу: %s", groupSub.getTitle()));
            } else {
                sendBotMessageService.sendMessage(chatId, "Не нашел такой группы =/");
            }
        } else {
            sendBotMessageService.sendMessage(chatId, "неправильный формат ID группы.\n " +
                    "ID должно быть целым положительным числом");
        }
    }

    private void sendGroupIdList(String chatId) {
        String message;
        List<GroupSub> groupSubs = telegramUserService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new)
                .getGroupSubs();
        if (CollectionUtils.isEmpty(groupSubs)) {
            message = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addGroupSub";
        } else {
            message = "Чтобы удалить подписку на группу - передай комадну вместе с ID группы. \n" +
                    "Например: /deleteGroupSub 16 \n\n" +
                    "я подготовил список всех групп, на которые ты подписан) \n\n" +
                    "имя группы - ID группы \n\n" +
                    "%s";

        }
        String userGroupSubData = groupSubs.stream()
                .map(group -> format("%s - %s \n", group.getTitle(), group.getId()))
                .collect(Collectors.joining());

        sendBotMessageService.sendMessage(chatId, format(message, userGroupSubData));
    }
}