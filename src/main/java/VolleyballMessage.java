import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

public class VolleyballMessage {
    private final VMessageType messageType;
    private final SendMessage message;
    private final SendLocation location;

    public VolleyballMessage(VMessageType messageType, SendMessage message, SendLocation location) {
        this.messageType = messageType;
        this.message = message;
        this.location = location;
    }

    public VMessageType getMessageType() {
        return messageType;
    }

    public SendMessage getMessage() {
        return message;
    }

    public SendLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolleyballMessage that = (VolleyballMessage) o;
        return messageType == that.messageType &&
                Objects.equals(message, that.message) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageType, message, location);
    }
}
