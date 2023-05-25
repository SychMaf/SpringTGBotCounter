package com.project.SpringTGBot.handler;

import com.project.SpringTGBot.components.Buttons;
import com.project.SpringTGBot.database.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Random;

@Slf4j
@Component
public class RandomizerCommand {
    @Autowired
    private UserRepository userRepository;
    final Random random = new Random();

    public SendMessage startRandomizeMessage(long chatId) {
        log.info("start RandomEvent operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("попробуй угадать число");
        message.setReplyMarkup(Buttons.numberLine());
        return message;
    }

    public SendMessage checkRandomNumber(long chatId, int userNum, long userId) {
        int luck = random.nextInt(2)+1;
        log.info("start check Num operation operation");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (luck == userNum) {
            message.setText("угадал + 1");
            userRepository.updateScoreByUserId(userId);
        } else {
            message.setText("не угадал => 0");
            userRepository.removeScoreByUserId(userId);
        }
        message.setReplyMarkup(Buttons.numberLine());
        return message;
    }

    public SendMessage getScore(long userId, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("твой счет: " + userRepository.findUserScoreById(userId));
        return message;
    }
}
