package com.eros.demo.openai.config;

import com.eros.demo.openai.advisors.TokenUsageAuditAdvisor;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
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
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.8)
                .model("gpt-4.1-mini")
                .maxTokens(100)
                .build();
        return ChatClient.builder(openAiChatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())
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



    @Bean(name = "openAiClientVanilla")
    public ChatClient openAiClientVanilla() {
        ChatOptions chatOptions = ChatOptions.builder()
//                .temperature(0.8)
                .model("gpt-4.1-mini")
//                .maxTokens(100)
                .build();
        return ChatClient.builder(openAiChatModel)
                .defaultOptions(chatOptions)
                .build();
    }


//    @Bean(name = "ollamaAiClient")
//    public ChatClient ollamaAiClient() {
//        ChatClient.Builder builder = ChatClient.builder(ollamaChatModel);
//        return builder.build();
//    }


}
