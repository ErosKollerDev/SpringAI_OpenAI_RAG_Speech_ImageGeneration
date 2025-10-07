package com.eros.demo.openai.config;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eros.demo.openai.processor.PIIMaskingDocumentPostProcessor;
import com.eros.demo.openai.advisors.TokenUsageAuditAdvisor;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ChatMemoryClientConfig {

//    private final OpenAiChatModel openAiChatModel;
//    private final ChatMemory chatMemory;
//    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

//    @Bean(name = "chatMemory")
//    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
//        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
//                .maxMessages(10)
//                .chatMemoryRepository(jdbcChatMemoryRepository)
//                .build();
//        return messageWindowChatMemory;
//    }

    @Bean(name = "openAiClientMemory")
    public ChatClient openAiClientMemory(ChatClient.Builder chatClientBuilder,
                                         @Qualifier("chatMemory") ChatMemory chatMemory, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        TokenUsageAuditAdvisor tokenUsageAuditAdvisor = new TokenUsageAuditAdvisor();
        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.8)
                .model("gpt-4.1-mini")
                .maxTokens(100)
                .build();
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(chatMemoryAdvisor, simpleLoggerAdvisor, tokenUsageAuditAdvisor, retrievalAugmentationAdvisor))
                .defaultOptions(chatOptions)
                .build();
    }


//    @Bean(name = "openAiClientMemoryForTime")
//    public ChatClient openAiClientMemoryForTime(ChatClient.Builder chatClientBuilder,
//                                         @Qualifier("chatMemory") ChatMemory chatMemory, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
//        TokenUsageAuditAdvisor tokenUsageAuditAdvisor = new TokenUsageAuditAdvisor();
//        ChatOptions chatOptions = ChatOptions.builder()
//                .temperature(0.8)
//                .model("gpt-4.1-mini")
//                .maxTokens(100)
//                .build();
//        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
//        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
//        return chatClientBuilder
//                .defaultAdvisors(List.of(chatMemoryAdvisor, simpleLoggerAdvisor, tokenUsageAuditAdvisor, retrievalAugmentationAdvisor))
//                .defaultOptions(chatOptions)
//                .build();
//    }


    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                                .queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(chatClientBuilder.clone()).targetLanguage("english").build())
                .documentRetriever(
                        VectorStoreDocumentRetriever
                                .builder()
                                .vectorStore(vectorStore).topK(3).similarityThreshold(0.5).build())
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
                .build();
    }


}
