package com.eros.demo.openai.config;

import com.eros.demo.openai.advisors.TokenUsageAuditAdvisor;
import com.eros.demo.openai.service.HelpDeskService;
import com.eros.demo.openai.tools.HelpDeskTools;
import com.eros.demo.openai.tools.TimeTools;
import com.eros.demo.openai.tools.WeatherTools;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class HelpDeskChatClientConfig {

    @Value("classpath:/promptTemplates/helpDeskSystemPromptTemplate.st")
    Resource systemPromptTemplate;


    @Bean("helpDeskChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory, TimeTools timeTools, WeatherTools weatherTools) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultSystem(systemPromptTemplate)
                .defaultTools(timeTools, weatherTools)
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor, tokenUsageAdvisor))
                .build();
    }

}
