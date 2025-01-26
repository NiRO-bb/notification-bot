package com.github.notification_bot.bot;

import com.github.notification_bot.bot.command.Command;
import com.github.notification_bot.bot.command.UnknownCommand;
import org.junit.jupiter.api.DisplayName;

import static com.github.notification_bot.bot.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level testing for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "/fdgdfgdfgdbd";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}