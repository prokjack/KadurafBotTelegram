package com.j2ck.stereogram;

import com.j2ck.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class StereogramBotTest {
    StereogramBot stereogramBot;
    @BeforeEach
    public void setUp() throws Exception {
        stereogramBot = spy(new StereogramBot());
    }

    @Test
    public void handleRequest() {
        StereogramMessage test = stereogramBot.handleRequest("/text test", 111L);
        assertEquals(MessageType.IMAGE_RESPONSE, test.getMessageType());
    }
}
