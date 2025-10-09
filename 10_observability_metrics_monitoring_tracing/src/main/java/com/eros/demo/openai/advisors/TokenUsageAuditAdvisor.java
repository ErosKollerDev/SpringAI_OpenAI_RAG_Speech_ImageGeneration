package com.eros.demo.openai.advisors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
public class TokenUsageAuditAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        Integer completionTokens = chatResponse.getMetadata().getUsage().getCompletionTokens();
        Integer promptTokens = chatResponse.getMetadata().getUsage().getPromptTokens();
        Integer totalTokens = chatResponse.getMetadata().getUsage().getTotalTokens();
        log.debug("Completion completionTokens used: {}", completionTokens);
        log.debug("Completion promptTokens used: {}", promptTokens);
        log.debug("Completion totalTokens used: {}", totalTokens);
        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
