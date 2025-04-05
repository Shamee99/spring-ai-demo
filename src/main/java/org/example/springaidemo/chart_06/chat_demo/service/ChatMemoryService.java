package org.example.springaidemo.chart_06.chat_demo.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * 对话记忆管理
 */
@Service
public class ChatMemoryService {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    private final ChatMemory chatMemory;
    public ChatMemoryService(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }


    public Flux<String> chatWithMemoryStream(String conversationId, String message) {

        ChatClient.StreamResponseSpec resp = ChatClient.builder(openAiChatModel)
                // 设置历史对话的保存方式，这里我们使用内存保存
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory))
                .build()
                .prompt().user(message)
                .advisors(advisor ->
                        // 设置保存的历史对话ID
                        advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                                // 设置需要保存几轮的历史对话，用于避免内存溢出，因为这里我们没做持久化
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50)
                ).stream();
        return resp.content();
    }

}

@Configuration
class AiConfig {

    /**
     * 会话记忆管理器
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
