package com.github.notification_bot.bot;

import com.github.notification_bot.bot.command.Command;
import com.github.notification_bot.bot.command.StopCommand;
import org.junit.jupiter.api.DisplayName;

import static com.github.notification_bot.bot.command.CommandName.STOP;
import static com.github.notification_bot.bot.command.StopCommand.STOP_COMMAND;

@DisplayName("Unit-level testing for StopCommand")
public class StopCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return STOP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return STOP_COMMAND;
    }

    @Override
    Command getCommand() {
        return new StopCommand(sendBotMessageService);
    }
}