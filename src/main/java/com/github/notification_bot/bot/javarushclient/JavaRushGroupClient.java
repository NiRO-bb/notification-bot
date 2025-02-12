package com.github.notification_bot.bot.javarushclient;

import com.github.notification_bot.bot.javarushclient.dto.GroupDiscussionInfo;
import com.github.notification_bot.bot.javarushclient.dto.GroupInfo;
import com.github.notification_bot.bot.javarushclient.dto.GroupRequestArgs;
import com.github.notification_bot.bot.javarushclient.dto.GroupsCountRequestArgs;

import java.util.List;


public interface JavaRushGroupClient {


    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);


    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);


    Integer getGroupCount(GroupsCountRequestArgs countRequestArgs);


    GroupDiscussionInfo getGroupById(Integer id);
}