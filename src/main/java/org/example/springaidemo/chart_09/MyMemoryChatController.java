package org.example.springaidemo.chart_09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@RestController
public class MyMemoryChatController {

    private final ChatClient chatClient;
    public MyMemoryChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    // 定义一个集合，将所有问答信息保存起来
    private final List<Message> historyMessages = new CopyOnWriteArrayList<>();

    @GetMapping("/my_memory")
    public String my_memory(@RequestParam("text") String text) {
        UserMessage userMessage = new UserMessage(text);
        historyMessages.add(userMessage);

        // 将保存的历史信息传递给 ChatClient
        String content = chatClient.prompt(new Prompt(historyMessages)).call().content();
        System.out.println(content);
        return content;
    }
}
