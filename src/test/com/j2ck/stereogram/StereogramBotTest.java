package com.j2ck.stereogram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.spy;

public class StereogramBotTest {
    StereogramBot stereogramBot;
    @Before
    public void setUp() throws Exception {
        stereogramBot = spy(new StereogramBot());
    }

    @Test
    public void handleRequest() {
        StereogramMessage test = stereogramBot.handleRequest("/text test", 111L);
        Assert.assertEquals(test.getMessageType(), StereogramMessageType.IMAGE_RESPONSE);
    }
}
