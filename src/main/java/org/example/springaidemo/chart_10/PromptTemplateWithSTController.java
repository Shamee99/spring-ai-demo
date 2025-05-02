package org.example.springaidemo.chart_10;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PromptTemplateWithSTController {

    @Value("classpath:/templates/user-message.st")
    private Resource promptUserMessage;

    private final ChatClient chatClient;
    public PromptTemplateWithSTController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/st")
    public String st() {
        PromptTemplate promptTemplate = new PromptTemplate(promptUserMessage);
        Prompt prompt = promptTemplate.create(Map.of("subject1", "程序员", "subject2", "打工人"));
        return chatClient.prompt(prompt)
                .call().content();
    }

}
