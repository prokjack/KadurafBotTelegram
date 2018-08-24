package com.j2ck;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Optional;

public class Bot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {
// We check if the update has a message and the message has text
        Optional.ofNullable(update.getMessage()).map(Message::getText)
                .map(text -> handleRequest(text, update.getMessage().getChatId())).ifPresent(method -> {
                    try {
                        if (method.getMessageType().equals(VMessageType.SCHEDULED_GAME)) {
                            execute(method.getMessage());
                            execute(method.getLocation());
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private VolleyballMessage handleRequest(String text, Long chatId) {
        if (text.contains("/nextgame")) {
                SendMessage sendMessage = new SendMessage(chatId, "Next game scheduled on 25/08/2018 at 5:30PM");
                SendLocation sendLocation = new SendLocation(32.073998f, 34.764729f);
                sendLocation.setChatId(chatId);
                return new VolleyballMessage(VMessageType.SCHEDULED_GAME, sendMessage, sendLocation);
        }
        return null;
    }


    public String getBotUsername() {
        return "KadurafBot";
    }

    public String getBotToken() {
        return "677234379:AAHtxsZ8lgO_2lXQIKtGsj1wTKaEB-l9cDk";
    }

    public void clearWebhook() throws TelegramApiRequestException {

    }
}
