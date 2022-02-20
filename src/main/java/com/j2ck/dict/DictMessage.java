package com.j2ck.dict;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DictMessage extends Message {
    private final DictMessageType messageType;
    private final SendMessage message;

    public DictMessage(DictMessageType messageType, SendMessage message) {
        this.messageType = messageType;
        this.message = message;
    }

    public DictMessageType getMessageType() {
        return messageType;
    }

    public SendMessage getMessage() {
        return message;
    }
}
