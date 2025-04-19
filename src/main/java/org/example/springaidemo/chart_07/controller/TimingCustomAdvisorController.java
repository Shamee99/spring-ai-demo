package org.example.springaidemo.chart_07.controller;


import org.example.springaidemo.chart_07.advisor.FormatCustomAdvisor;
import org.example.springaidemo.chart_07.advisor.TimingCustomAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisor")
public class TimingCustomAdvisorController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @GetMapping("/timing")
    public void timing() {
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .build();

        // 将自定义的advisor添加进去
        TimingCustomAdvisor timingCustomAdvisor = new TimingCustomAdvisor();
        // 自定义第二个advisor
        FormatCustomAdvisor formatCustomAdvisor = new FormatCustomAdvisor();
        ChatClient.CallResponseSpec response = chatClient.prompt()
                .advisors(timingCustomAdvisor, formatCustomAdvisor)
                .user("李白对美国加关税怎么看？").call();

        System.out.println(response.content());
    }

}
