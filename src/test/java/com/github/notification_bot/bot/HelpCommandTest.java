package com.github.notification_bot.bot;

import com.github.notification_bot.bot.command.Command;
import com.github.notification_bot.bot.command.HelpCommand;
import org.junit.jupiter.api.DisplayName;

import static com.github.notification_bot.bot.command.CommandName.*;
import static com.github.notification_bot.bot.command.HelpCommand.HELP_MESSAGE;

@DisplayName("Unit-level testing for HelpCommand")
public class HelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new HelpCommand(sendBotMessageService);
    }
}