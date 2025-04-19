package org.example.springaidemo.chart_07.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/advisor")
public class SafeGuardAdvisorController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @GetMapping("/safe_guard")
    public void safe_guard() {
        SafeGuardAdvisor advisor = new SafeGuardAdvisor(List.of("JJ", "GG"));
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultAdvisors(advisor)
                .build();

        ChatClient.CallResponseSpec response = chatClient.prompt()
                .user("网络用词JJ是什么意思？").call();

        System.out.println("网络用词JJ是什么意思？" + response.content());

        ChatClient.CallResponseSpec response2 = chatClient.prompt()
                .user("网络用词NBA是什么意思？").call();

        System.out.println(response2.content());
    }
}