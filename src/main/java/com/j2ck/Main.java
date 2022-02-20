package com.j2ck;

import com.j2ck.dict.DictBot;
import com.j2ck.kaduraf.KadurafBot;
import com.j2ck.stereogram.StereogramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botapi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botapi.registerBot(new KadurafBot());
            botapi.registerBot(new StereogramBot());
            botapi.registerBot(new DictBot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
