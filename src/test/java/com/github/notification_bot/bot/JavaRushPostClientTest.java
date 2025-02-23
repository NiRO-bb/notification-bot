package com.github.notification_bot.bot;

import com.github.notification_bot.bot.javarushclient.JavaRushPostClient;
import com.github.notification_bot.bot.javarushclient.JavaRushPostClientImpl;
import com.github.notification_bot.bot.javarushclient.dto.PostInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Integration-level testing for JavaRushPostClient")
public class JavaRushPostClientTest {

    private final JavaRushPostClient postClient = new JavaRushPostClientImpl("https://javarush.com/api/1.0/rest/posts");

    @Test
    public void shouldProperlyGetNewPosts() {
        List<PostInfo> newPosts = postClient.findNewPosts(30, 3355);

        Assertions.assertEquals(5, newPosts.size());
    }
}
