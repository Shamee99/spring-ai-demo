package org.example.springaidemo.chart_03.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptTemplateController {


    private final ChatClient chatClient;
    public PromptTemplateController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/promptTemplate")
    public String promptTemplate() {
        // 使用系统消息预定好角色风格
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate ("你是一位风格{style}的演说家");
        Message systemMessage = systemTemplate.createMessage(Map.of("style", "幽默诙谐"));

        PromptTemplate userTemplate = new PromptTemplate("请介绍一下自己的{store}");
        Message userMessage = userTemplate.createMessage(Map.of("store", "情感经历"));


        List<Message> messages = List.of(systemMessage, userMessage);
        String content = chatClient.prompt(new Prompt(messages)).call().content();
        System.out.println(content);
        return content;
    }
}
