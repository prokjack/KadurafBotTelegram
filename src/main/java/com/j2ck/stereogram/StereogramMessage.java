package com.j2ck.stereogram;

import com.j2ck.BotMessage;
import com.j2ck.MessageType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@EqualsAndHashCode(callSuper = true)
class StereogramMessage extends BotMessage {
    private MessageType messageType;
    private SendPhoto photo;

    StereogramMessage(Boolean success, MessageType messageType, SendPhoto photo) {
        super(success);
        this.messageType = messageType;
        this.photo = photo;
    }

    public StereogramMessage(Boolean success) {
        super(success);
    }
}
