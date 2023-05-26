package com.project.SpringTGBot.handler;

import com.project.SpringTGBot.components.Buttons;
import com.project.SpringTGBot.database.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class DefaultCommand {
    @Autowired
    private UserRepository userRepository;
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

    public SendMessage getUserMessageCount(long chatId, long userId, String userName) {
        log.info("try return count");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        Integer count = userRepository.findUserCountById(chatId, userId);
        if (count == null) {
            count = 0;
        }
        message.setText("Hi, " + userName + " You send " + count + " message!");
        return message;
    }

}
