package com.github.notification_bot.bot.javarushclient.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=false)
@Data
@ToString(callSuper=false)
public class GroupDiscussionInfo extends GroupInfo {
    private UserDiscussionInfo userDiscussionInfo;
    private Integer commentCount;
}
