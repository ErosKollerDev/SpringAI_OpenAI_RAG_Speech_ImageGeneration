package com.eros.demo.openai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/memory")
public class MemoryChatController{

    private final ChatClient openAiClient;

    public MemoryChatController( @Qualifier("openAiClientMemory") ChatClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> openAI(@RequestHeader String username, @RequestParam String message) {

            String content = this.openAiClient
                    .prompt()
                    .user(message)
//                    .advisors( adv -> adv.param("CONVERSATION_ID", username))
                    .advisors( adv -> adv.param(ChatMemory.CONVERSATION_ID, username))
                    .call()
                    .content();
            log.info("Successfully processed chat request");
            return ResponseEntity.ok(("\nMessage -> %s" +
                    "\nAnswer: %s").formatted(message, content));
    }
}
