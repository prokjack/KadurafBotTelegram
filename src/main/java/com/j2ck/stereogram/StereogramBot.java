package com.j2ck.stereogram;

import com.j2ck.stereogram.generator.ImageManipulator;
import com.j2ck.stereogram.generator.StereogramGenerator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

public class StereogramBot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {
// We check if the update has a message and the message has text
        Optional.ofNullable(update.getMessage()).map(Message::getText)
                .map(text -> handleRequest(text, update.getMessage().getChatId())).ifPresent(method -> {
                    try {
                        if (method.getSuccess()) {
                            if (method.getMessageType().equals(StereogramMessageType.IMAGE_RESPONSE)) {
                                execute(method.getPhoto());
                            }
                        } else {
                            System.out.println("Unsuccessful generation");
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public StereogramMessage handleRequest(String text, Long chatId) {
        if (text.contains("/text")) {
            int width = 640;
            int height = 480;
            int fontSize = 150;
            String textToHandle;
            String[] s = text.split(" ");
            if (s.length > 1) {
                textToHandle = s[1];
            } else {
                System.out.println("Text is incorrect");
                return new StereogramMessage(false);
            }
            BufferedImage textDepthMap = ImageManipulator.generateTextDepthMap(textToHandle, fontSize, width, height);

            BufferedImage texturePattern;
            try {
                texturePattern = getImage(getClass().getResourceAsStream("/resources/texture/RAND4.jpg"));
                BufferedImage stereogram = StereogramGenerator.generateTexturedSIRD(
                        textDepthMap, texturePattern,
                        width, height,
                        20, 2.5f,
                        2000, 0,
                        80, 80);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(stereogram, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setPhoto(textToHandle, is);
                return new StereogramMessage(true, StereogramMessageType.IMAGE_RESPONSE, sendPhoto);
            } catch (Exception e) {
                return new StereogramMessage(false);
            }


        }
        return null;
    }

    private BufferedImage getImage(InputStream file) throws Exception {
        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            throw new Exception("Error while loading image." +
                    System.getProperty("line.separator") +
                    "ERROR: " + e.getMessage());
        }
    }

    public String getBotUsername() {
        return "StereogramGenBot";
    }

    public String getBotToken() {
        return "1243333766:AAFeT8ShUwZcHNjIRCh5cObnUJbVoZe8i4Y";
    }

    public void clearWebhook() throws TelegramApiRequestException {

    }
}
