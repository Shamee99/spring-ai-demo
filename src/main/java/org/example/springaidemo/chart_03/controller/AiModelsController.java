package org.example.springaidemo.chart_03.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiModelsController {


    private final ChatClient chatClient;
    public AiModelsController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat() {
        String content = chatClient.prompt().user("你是谁？").call().content();
        System.out.println(content);
        return content;
    }
}