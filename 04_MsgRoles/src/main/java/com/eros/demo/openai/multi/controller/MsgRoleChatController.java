package com.eros.demo.openai.multi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class MsgRoleChatController {


    private final ChatClient openAiClient;
//    private final ChatClient ollamaAiClient;

    public MsgRoleChatController(
            @Qualifier("openAiClient") ChatClient openAiClient
//            @Qualifier("ollamaAiClient") ChatClient ollamaAiClient
    ) {
//        this.ollamaAiClient = ollamaAiClient;
        this.openAiClient = openAiClient;
    }

    @GetMapping("/openai/chat")
    public ResponseEntity<String> openAI(@RequestParam String message) {
        try {
            String content = this.openAiClient
                    .prompt()
//                    .system()
                    .user(message)
                    .call().content();
            log.info("Successfully processed chat request");
            return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error processing your request");
        }
    }


    @GetMapping("/openai/chat/unhinge")
    public ResponseEntity<String> openAIUnhinge(@RequestParam String message) {
        try {
            String content = this.openAiClient
                    .prompt()
                    .system("""
                            You are an AI assistant working like a pimp. You must:
                            - Be straight and rude
                            - Go fucking around
                            - Usa casual language
                            - Be brave, silence and brutally honest
                            - Focus on answer fast and short
                            - Anything else if you don't like, kindly inform them to fuck off
                            - You don't have time to spend with stupid people
                            - FAFO -> Fuck Around Find Out What's Off
                            """)
                    .user(message)
                    .call().content();
            log.info("Successfully processed chat request");
            return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error processing your request");
        }
    }

//    @GetMapping("/ollama/chat")
//    public ResponseEntity<String> ollama(@RequestParam String message) {
//        String content = this.ollamaAiClient.prompt(message).call().content();
//        return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
//    }
}
