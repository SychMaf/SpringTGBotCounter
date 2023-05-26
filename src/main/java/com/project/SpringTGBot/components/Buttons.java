package com.project.SpringTGBot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton RANDOM = new InlineKeyboardButton("Random");
    private static final InlineKeyboardButton FIRST = new InlineKeyboardButton("1");
    private static final InlineKeyboardButton SECOND = new InlineKeyboardButton("2");
    private static final InlineKeyboardButton SCORE = new InlineKeyboardButton("score");
    private static final InlineKeyboardButton COUNT = new InlineKeyboardButton("Count Message");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        RANDOM.setCallbackData("/random");
        COUNT.setCallbackData("/count");

        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON, HELP_BUTTON);
        List<InlineKeyboardButton> anotherLine = List.of(RANDOM);
        List<InlineKeyboardButton> countLine = List.of(COUNT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, countLine, anotherLine);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup numberLine() {
        FIRST.setCallbackData("/1");
        SECOND.setCallbackData("/2");
        SCORE.setCallbackData("/score");
        List<InlineKeyboardButton> rowInline = List.of(FIRST, SECOND);
        List<InlineKeyboardButton> anotherLine = List.of(SCORE);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, anotherLine);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
