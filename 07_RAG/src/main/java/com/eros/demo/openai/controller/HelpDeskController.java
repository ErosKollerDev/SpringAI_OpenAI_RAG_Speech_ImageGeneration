package com.eros.demo.openai.controller;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.ai.chat.client.ChatClient;

import com.eros.demo.openai.tools.HelpDeskTools;

import java.util.Map;

@RestController
@RequestMapping("/api/tools")
public class HelpDeskController {


    private final ChatClient chatClient;
    private final HelpDeskTools helpDeskTools;


    public HelpDeskController(@Qualifier("helpDeskChatClient") ChatClient chatClient, HelpDeskTools helpDeskTools) {
        this.chatClient = chatClient;
        this.helpDeskTools = helpDeskTools;
    }


    @GetMapping("/help-desk")
    public ResponseEntity<String> helpDesk(@RequestHeader("username") String username, @RequestParam String message) {
        String content = this.chatClient.prompt()
                .advisors(a -> a.param("CONVERSATION_ID", username))
                .user(message)
                .tools(helpDeskTools)
                .toolContext(Map.of("username", username))
                .call().content();
        return ResponseEntity.ok(content);
    }

}
