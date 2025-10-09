package com.eros.demo.openai.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StreamController {

    private final ChatClient openAiClient;

    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> chatStream(@RequestParam String message) {

        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .temperature(0.8)
                .model("gpt-4.1-mini")
                .maxTokens(300)
                .presencePenalty(0.6)
//                .metadata(Map.of("Porra", "Ce Loko"))
                .build();
        Flux<String> fluxContent = this.openAiClient
                .prompt()
//                .system("Be your self.")
                .options(openAiChatOptions)
                .user(message)
                .stream().content();
        return ResponseEntity.ok(fluxContent);
    }


}
