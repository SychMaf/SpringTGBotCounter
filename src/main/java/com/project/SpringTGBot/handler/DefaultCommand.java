package com.project.SpringTGBot.handler;

import com.project.SpringTGBot.components.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class DefaultCommand {
    String HELP_TEXT = """
            This bot will help to count the number of messages in the chat. The following commands are available to you:

            /start - start the bot
            /help - help menu
            /random - попробовать угадать число""";

    public SendMessage startBotOperation(long chatId, String userName) {
        log.info("start message operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hi, " + userName + "! I'm a Telegram bot.'");
        message.setReplyMarkup(Buttons.inlineMarkup());
        return message;
    }

    public SendMessage helpBotOperation(long chatId) {
        log.info("help message operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(HELP_TEXT);
        return message;
    }

}
