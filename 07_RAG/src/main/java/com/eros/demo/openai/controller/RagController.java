package com.eros.demo.openai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@RestController
@RequestMapping("/api/rag")
public class RagController {


    private final ChatClient openAiClient;
    private final ChatClient webSearchchatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/promptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/hrAssistantSystemPromptTemplate.st")
    Resource hrAssistantSystemPromptTemplate;


    public RagController(
            VectorStore vectorStore,
            @Qualifier(value = "openAiClientMemory") ChatClient openAiClient,
            @Qualifier("webSearchRAGChatClient") ChatClient webSearchchatClient) {
        this.openAiClient = openAiClient;
        this.vectorStore = vectorStore;
        this.webSearchchatClient = webSearchchatClient;
    }


    @GetMapping("/chat")
    public ResponseEntity<String> chatTextParams(@RequestHeader String username, @RequestParam String message) {
        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> documentList = this.vectorStore.similaritySearch(searchRequest);
        String similarContextText = documentList.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String content = this.openAiClient
                .prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
                        .param("documents", similarContextText))
                .user(message)
                .advisors(adv -> adv.param(CONVERSATION_ID, username))
                .call().content();
        return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
    }

    @GetMapping("/document")
    public ResponseEntity<String> chatPdfDocumentParams(@RequestHeader String username, @RequestParam String message) {
//        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
//        List<Document> documentList = this.vectorStore.similaritySearch(searchRequest);
//        String similarContextText = documentList.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String content = this.openAiClient
                .prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(hrAssistantSystemPromptTemplate)
//                        .param("documents", similarContextText)
                )
                .user(message)
                .advisors(adv -> adv.param(CONVERSATION_ID, username))
                .call().content();
        return ResponseEntity.ok("Chat -> %s\nAnswer: %s".formatted(message, content));
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
