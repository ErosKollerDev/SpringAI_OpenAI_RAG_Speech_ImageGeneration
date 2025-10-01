package com.eros.demo.openai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/prompt")
public class PromptTemplateController {


    private final ChatClient openAiClient;


    @Value("classpath:/promptTemplates/promptTemplateForEmail.st")
    private Resource promptEmailTemplate;

    public PromptTemplateController(@Qualifier("openAiClient") ChatClient openAiClient) {
        this.openAiClient = openAiClient;
    }


    @GetMapping("/email/chat")
    public ResponseEntity<String> emailAI(@RequestParam String customerName,
                                         @RequestParam String customerMessage) {
        try {
            String content = this.openAiClient
                    .prompt()
                    .system("""
                            You are an AI assistant for drafting a email body. You must:
                            - Professional
                            - Cordial
                            - Friendly
                            - Reliable
                            - Elegant
                            """)
                    .user(promptUserSpec ->
                            promptUserSpec.text(promptEmailTemplate)
                                    .param("customerName", customerName)
                                    .param("customerMessage", customerMessage))
                    .call().content();
            log.info("Successfully processed chat request");
            return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(customerMessage, content));
        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error processing your request");
        }
    }
}
