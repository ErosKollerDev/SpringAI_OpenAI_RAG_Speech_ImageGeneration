package com.eros.demo.openai.config;

import com.eros.demo.openai.advisors.TokenUsageAuditAdvisor;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class ChatMemoryClientConfig {

    private final OpenAiChatModel openAiChatModel;
    private final ChatMemory chatMemory;

    @Bean(name = "openAiClientMemory")
    public ChatClient openAiClientMemory() {
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.8)
                .model("gpt-4.1-mini")
                .maxTokens(100)
                .build();
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return ChatClient.
                builder(openAiChatModel)
                .defaultAdvisors(List.of(chatMemoryAdvisor, simpleLoggerAdvisor))
                .build();
    }


}
