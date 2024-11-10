package com.maimai.nlsearch;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NlSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(NlSearchApplication.class, args);
    }

    @Bean
    public ChatClient claudeAIClient(AnthropicChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

}
