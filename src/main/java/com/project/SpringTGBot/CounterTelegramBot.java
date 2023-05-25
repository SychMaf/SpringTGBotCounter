package com.project.SpringTGBot;

import com.project.SpringTGBot.components.BotCommands;
import com.project.SpringTGBot.components.Buttons;
import com.project.SpringTGBot.config.BotConfig;
import com.project.SpringTGBot.database.User;
import com.project.SpringTGBot.database.UserRepository;
import com.project.SpringTGBot.handler.DefaultCommand;
import com.project.SpringTGBot.handler.RandomizerCommand;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Random;

@Slf4j
@Component
public class CounterTelegramBot extends TelegramLongPollingBot implements BotCommands {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DefaultCommand defaultCommand;
    @Autowired
    private RandomizerCommand randomizerCommand;
    final BotConfig config;
    final Random random = new Random();

    public CounterTelegramBot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        /*if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();

                try {
                    botAnswerUtils(receivedMessage, chatId, userName, userId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else*/ if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            try {
                botAnswerUtils(receivedMessage, chatId, userName, userId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if(chatId == Long.valueOf(config.getChatId())){
            updateDB(userId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName, long userId) throws TelegramApiException {
        int luck = random.nextInt(2)+1;
        switch (receivedMessage){
            case "/start":
                try {
                    execute(defaultCommand.startBotOperation(chatId,userName));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/help":
                try {
                    execute(defaultCommand.helpBotOperation(chatId));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/random":
                    execute(randomizerCommand.startRandomizeMessage(chatId));
                break;
            case "/1":
                if (luck == 1) {
                    choice(chatId, true, userId);
                } else {
                    choice(chatId, false, userId);
                }
                break;
            case "/2":
                if (luck == 2) {
                    choice(chatId, true, userId);
                } else {
                    choice(chatId, false, userId);
                }
                break;
            case "/score":
                getScore(userId,chatId);
                break;
            default: break;
        }
    }

    private void choice(long chatId, boolean back, long userId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (back) {
            message.setText("угадал + 1");
            updateScoreDB(userId, true);
        } else {
            message.setText("не угадал => 0");
            updateScoreDB(userId, false);
        }
        message.setReplyMarkup(Buttons.numberLine());
        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void updateDB(long userId, String userName) {
        if(userRepository.findById(userId).isEmpty()){
            User user = new User();
            user.setId(userId);
            user.setName(userName);
            //сразу добавляем в столбец каунтера 1 сообщение
            user.setMsg_numb(1);

            userRepository.save(user);
            log.info("Added to DB: " + user);
        } else {
            userRepository.updateMsgNumberByUserId(userId);
        }
    }

    private void updateScoreDB(long userId, boolean plus) {
        if(plus) {
            userRepository.updateScoreByUserId(userId);
        } else {
            userRepository.removeScoreByUserId(userId);
        }
    }

    private void getScore(long userId, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("твой счет: " + userRepository.findUserById(userId));

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
