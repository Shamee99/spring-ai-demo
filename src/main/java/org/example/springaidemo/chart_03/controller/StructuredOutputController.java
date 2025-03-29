package org.example.springaidemo.chart_03.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StructuredOutputController {


    private final ChatClient chatClient;
    public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/structuredOutput")
    public String structuredOutput() {
        SystemMessage systemMessage = new SystemMessage("诙谐幽默风格");
        UserMessage userMessage = new UserMessage("你是谁？");

        List<Message> messages = List.of(systemMessage, userMessage);

        String content = chatClient.prompt(new Prompt(messages)).call().content();

        String con = new MyOutputConverter().convert(content);
        System.out.println(con);
        return content;
    }


}


class MyOutputConverter implements StructuredOutputConverter<String> {

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public String convert(String source) {
        return """
                我是通过自定义结构化输出的内容：
                ============================================
                """ + source;
    }
}
