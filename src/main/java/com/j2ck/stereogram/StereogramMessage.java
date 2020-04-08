package com.j2ck.stereogram;

import com.j2ck.BotMessage;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Getter
@EqualsAndHashCode(callSuper = true)
class StereogramMessage extends BotMessage {
    private StereogramMessageType messageType;
    private SendPhoto photo;

    StereogramMessage(Boolean success, StereogramMessageType messageType, SendPhoto photo) {
        super(success);
        this.messageType = messageType;
        this.photo = photo;
    }

    public StereogramMessage(Boolean success) {
        super(success);
    }
}
