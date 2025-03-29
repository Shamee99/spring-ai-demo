package org.example.springaidemo.chart_03.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromptsController {


    private final ChatClient chatClient;
    public PromptsController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        SystemMessage systemMessage = new SystemMessage("诙谐幽默风格");
        UserMessage userMessage = new UserMessage("你是谁？");

        List<Message> messages = List.of(systemMessage, userMessage);
        String content = chatClient.prompt(new Prompt(messages)).call().content();
        System.out.println(content);
        return content;
    }
}
