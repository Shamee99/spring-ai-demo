package org.example.springaidemo.chart_07.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisor")
public class VectorStoreChatMemoryAdvisorController {

    @Autowired
    private OpenAiChatModel openAiChatModel;
    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/vector_memory")
    public void vector_memory() {
        // 定义消息历史记录保存advisor
        VectorStoreChatMemoryAdvisor memoryAdvisor = VectorStoreChatMemoryAdvisor.builder(vectorStore).build();
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultAdvisors(memoryAdvisor)
                .build();

        ChatClient.CallResponseSpec response = chatClient.prompt()
                .user("上次推荐的相机型号是什么？")
                .advisors(advisor ->
                        advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, "user_123")
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5) // 加载最近 5 轮对话
                ).call();

        System.out.println("-----上次推荐的相机型号是什么？" + response.content());

    }
}


///**
// * 定义一个简单的向量库
// */
//@Configuration
//class VectorConfig {
//
//    @Bean
//    public SimpleVectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
//        return new SimpleVectorStore(embeddingModel);
//    }
//}