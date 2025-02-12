package com.github.notification_bot.bot.javarushclient.dto;

import lombok.Data;

@Data
public class UserDiscussionInfo {

    private Boolean isBookmarked;
    private Integer lastTime;
    private Integer newCommentsCount;
}