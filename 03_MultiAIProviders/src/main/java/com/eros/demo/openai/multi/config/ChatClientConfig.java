package com.eros.demo.openai.multi.config;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ChatClientConfig {


//    private final OpenAiChatModel openAiChatModel;
    private final OllamaChatModel ollamaChatModel;

//    @Bean(name = "openAiClient")
//    public ChatClient openAiClient() {
//        return ChatClient.builder(openAiChatModel).build();
//    }

    @Bean
    public ChatClient ollamaAiClient() {
        ChatClient.Builder builder = ChatClient.builder(ollamaChatModel);
        return builder.build();
    }


}
