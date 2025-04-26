package org.example.springaidemo.chart_09;

import org.example.springaidemo.chart_09.memory.FileCacheChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FileCacheChatMemoryController {

    private static final ChatMemory fileCacheMemory = new FileCacheChatMemory("F:\\MyFileCache");

    private final ChatClient chatClient;
    public FileCacheChatMemoryController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new MessageChatMemoryAdvisor(fileCacheMemory)).build();
    }

    @GetMapping("/file_memory")
    public String file_memory(@RequestParam String requestId, @RequestParam("text") String text) {
        UserMessage userMessage = new UserMessage(text);
        // 将保存的历史信息传递给 ChatClient
        String content = chatClient.prompt(new Prompt(userMessage))
                .advisors(
                        // 设置会话id
                advisor -> advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, requestId)
                        .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50)
        ).call().content();
        System.out.println(content);
        return content;
    }
}