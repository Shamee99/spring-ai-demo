package org.example.springaidemo.chart_07.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisor")
public class PromptChatMemoryAdvisorController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @GetMapping("/prompt_memory")
    public void prompt_memory() {
        // 定义消息历史记录保存advisor
        PromptChatMemoryAdvisor memoryAdvisor = new PromptChatMemoryAdvisor(new InMemoryChatMemory());
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultAdvisors(memoryAdvisor)
                .build();

        ChatClient.CallResponseSpec response = chatClient.prompt()
                .user("推荐适合新手的相机")
                .advisors(advisor ->
                        advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, "user_123")
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5) // 加载最近 5 轮对话
                ).call();

        System.out.println("-----推荐适合新手的相机：" + response.content());

        ChatClient.CallResponseSpec response2 = chatClient.prompt()
                .user("按照价格排序一下")
                .advisors(advisor ->
                        advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, "user_123")
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5) // 加载最近 5 轮对话
                ).call();

        System.out.println("-----按照价格排序一下：" + response2.content());

    }

}
