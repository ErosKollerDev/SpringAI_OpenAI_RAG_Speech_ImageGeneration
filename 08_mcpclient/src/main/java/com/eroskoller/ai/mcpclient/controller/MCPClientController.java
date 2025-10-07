package com.eroskoller.ai.mcpclient.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MCPClientController {


    private final ChatClient chatClient;

    public MCPClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @RequestMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt(message).call().content();
    }

}
