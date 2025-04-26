package org.example.springaidemo.chart_09;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoMemoryChatController {

    private final ChatClient chatClient;
    public NoMemoryChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/no_memory")
    public String no_memory(@RequestParam("text") String text) {
        UserMessage userMessage = new UserMessage(text);
        String content = chatClient.prompt(new Prompt(userMessage)).call().content();
        System.out.println(content);
        return content;
    }

}
