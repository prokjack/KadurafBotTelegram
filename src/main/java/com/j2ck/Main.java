package com.j2ck;

import com.j2ck.stereogram.StereogramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot());
            botapi.registerBot(new StereogramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
