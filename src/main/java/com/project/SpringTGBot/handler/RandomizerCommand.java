package com.project.SpringTGBot.handler;

import com.project.SpringTGBot.components.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class RandomizerCommand {

    public SendMessage startRandomizeMessage(long chatId) {
        log.info("start RandomEvent operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("попробуй угадать число");
        message.setReplyMarkup(Buttons.numberLine());
        return message;
    }
}
