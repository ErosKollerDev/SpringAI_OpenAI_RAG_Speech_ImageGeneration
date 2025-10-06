package com.eros.demo.openai.config;

import com.eros.demo.openai.advisors.TokenUsageAuditAdvisor;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class ChatMemoryClientConfig {

//    private final OpenAiChatModel openAiChatModel;
//    private final ChatMemory chatMemory;
//    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Bean(name = "chatMemory")
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        return messageWindowChatMemory;
    }

    @Bean(name = "openAiClientMemory")
    public ChatClient openAiClientMemory(ChatClient.Builder chatClientBuilder,
                                          @Qualifier("chatMemory") ChatMemory chatMemory   ) {
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.8)
                .model("gpt-4.1-mini")
                .maxTokens(100)
                .build();
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder.defaultAdvisors(chatMemoryAdvisor, simpleLoggerAdvisor).defaultOptions(chatOptions).build();
    }


}
