package com.j2ck.dict;

import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DictBot extends TelegramLongPollingSessionBot {
    private static final String google_sheets_credentials = "GOOGLE_SHEETS_CREDENTIALS";

    private final GoogleCredentials sheetsCredentials;
    private final HebrewParser hebrewParser = new HebrewParser();

    public DictBot() throws Exception {
        sheetsCredentials = getCredentials();
    }

    private GoogleCredentials getCredentials() throws Exception {
        String credentialsFile = new String(Base64.getDecoder().decode(System.getenv().get(google_sheets_credentials)));
        return ServiceAccountCredentials.fromStream(new ByteArrayInputStream(credentialsFile.getBytes())).createScoped(SheetsScopes.all());
    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optionalSession) {
        Optional.ofNullable(update.getMessage()).map(Message::getText)
                .map(text -> {
                    try {
                        return handleRequest(text, update.getMessage().getChatId(), optionalSession);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .ifPresent(message -> {
                            try {
                                if (message.getMessageType().equals(DictMessageType.APPROVE) || message.getMessageType().equals(DictMessageType.NOT_EXIST)) {
                                    execute(message.getMessage());
                                }
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    private DictMessage handleRequest(String text, Long chatId, Optional<Session> optionalSession) throws Exception {
        if (!optionalSession.map(s -> s.getAttribute("root")).isPresent()) {
            try {
                String rootWord = text;
                optionalSession.ifPresent(s -> s.setAttribute("root", rootWord));

                List<Result> results = hebrewParser.parse(rootWord);
                if (results != null && !results.isEmpty()) {
                    optionalSession.ifPresent(s -> s.setAttribute("parseResult", results));
                    String collect = results.stream().map(Result::toString).collect(Collectors.joining("\n"));
                    return new DictMessage(DictMessageType.APPROVE, new SendMessage(chatId.toString(), collect));
                }
                else {
                    optionalSession.ifPresent(s -> s.removeAttribute("root"));
                    return new DictMessage(DictMessageType.NOT_EXIST, new SendMessage(chatId.toString(), "Not exist"));
                }
            } catch (IOException e) {

            }
        } else {
            String root = (String) optionalSession.get().getAttribute("root");
            List<Result> parseResult = (List<Result>) optionalSession.get().getAttribute("parseResult");

            if (parseResult != null && !parseResult.isEmpty()) {
                Sheet sheets = new Sheet(sheetsCredentials);
                sheets.write(root, parseResult);
            }
            optionalSession.get().removeAttribute("root");
            optionalSession.get().removeAttribute("parseResult");
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return "dicthebrewbot";
    }

    @Override
    public String getBotToken() {
        return "5226565595:AAEUzULijDJC2t4whDgOxyWYPMtOzgff6Tc";
    }
}
