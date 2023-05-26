package com.project.SpringTGBot;

import com.project.SpringTGBot.components.BotCommands;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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

    public CounterTelegramBot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
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

        if(update.hasMessage()) {
            config.setChatId(update.getMessage().getChatId().toString());
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

            if (chatId == Long.parseLong(config.getChatId())) {
                updateDB(userId, userName, chatId);
            }

        } else if (update.hasCallbackQuery()) {
            config.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
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

        /*if (chatId == Long.parseLong(config.getChatId())) {
            updateDB(userId, userName, chatId);
        }*/
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName, long userId) throws TelegramApiException {
        switch (receivedMessage) {
            case "/start" -> execute(defaultCommand.startBotOperation(chatId, userName));
            case "/help" -> execute(defaultCommand.helpBotOperation(chatId));
            case "/random" -> execute(randomizerCommand.startRandomizeMessage(chatId));
            case "/count" -> execute(defaultCommand.getUserMessageCount(chatId, userId, userName));
            case "/1" -> execute(randomizerCommand.checkRandomNumber(chatId, 1, userId));
            case "/2" -> execute(randomizerCommand.checkRandomNumber(chatId, 2, userId));
            case "/score" -> execute((randomizerCommand.getScore(userId, chatId)));
            default -> {
            }
        }
    }

    private void updateDB(long userId, String userName, long chatId) {
        if (userRepository.findById(chatId/2 + userId/3).isEmpty() || userRepository.findUserChatById(chatId, userId) == null) {
            User user = new User();
            user.setId(userId);
            user.setName(userName);
            user.setMsg_numb(1);
            user.setChatId(chatId);

            user.setUserKey(chatId/2 + userId/3);

            userRepository.save(user);
            log.info("Added to DB: " + user);
        } else {
            userRepository.updateMsgNumberByUserId(userId, chatId);
        }
    }

}
