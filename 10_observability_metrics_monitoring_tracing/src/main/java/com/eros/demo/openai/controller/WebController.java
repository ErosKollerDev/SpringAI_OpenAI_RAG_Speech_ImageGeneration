package com.eros.demo.openai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/v1/web-search")
public class WebController {


    private final ChatClient webSearchchatClient;

    public WebController(@Qualifier("webSearchRAGChatClient") ChatClient webSearchchatClient) {
        this.webSearchchatClient = webSearchchatClient;
    }

    @GetMapping("/web-search")
    public ResponseEntity<String> webSearchChat(@RequestHeader("username")
                                                String username, @RequestParam("message") String message) {
        String answer = webSearchchatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }

}
