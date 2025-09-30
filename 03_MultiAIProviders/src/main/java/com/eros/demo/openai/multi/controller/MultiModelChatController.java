package com.eros.demo.openai.multi.controller;


import lombok.AllArgsConstructor;
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
public class MultiModelChatController {


//    private final ChatClient openAiClient;
    private final ChatClient ollamaAiClient;

    public MultiModelChatController(
                                    @Qualifier("ollamaAiClient") ChatClient ollamaAiClient) {
//        this.openAiClient = openAiClient;
        this.ollamaAiClient = ollamaAiClient;
    }

//    @GetMapping("/openai/chat")
//    public ResponseEntity<String> openAI(@RequestParam String message) {
//        String content = this.openAiClient.prompt(message).call().content();
//        return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
//    }

    @GetMapping("/ollama/chat")
    public ResponseEntity<String> ollama(@RequestParam String message) {
        String content = this.ollamaAiClient.prompt(message).call().content();
        return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
    }
}
