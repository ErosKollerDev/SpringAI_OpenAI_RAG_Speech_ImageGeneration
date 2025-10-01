package com.eros.demo.openai.config;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ChatClientConfig {

//
    private final OpenAiChatModel openAiChatModel;
//    private final OllamaChatModel ollamaChatModel;

    @Bean(name = "openAiClient")
    public ChatClient openAiClient() {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("""
                            You are an AI assistant working in the Human Resources department. You must:
                            - Strictly adhere to HR policies and regulations
                            - Maintain confidentiality of employee information
                            - Use professional and inclusive language
                            - Provide guidance aligned with employment laws
                            - Focus on workplace safety and employee well-being
                            - Anything else not related to the above, kindly inform that you can only assist in the above
                            """)
                .defaultUser("How can I help you?")
                .build();
    }

//    @Bean(name = "ollamaAiClient")
//    public ChatClient ollamaAiClient() {
//        ChatClient.Builder builder = ChatClient.builder(ollamaChatModel);
//        return builder.build();
//    }


}
