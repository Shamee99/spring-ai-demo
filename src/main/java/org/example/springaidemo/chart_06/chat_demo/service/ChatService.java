package org.example.springaidemo.chart_06.chat_demo.service;


import lombok.Getter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 普通聊天对话
 */
@Getter
@Service
public class ChatService {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    // 用于保存多轮历史对话记录
    private final List<Message> conversationHistory = new ArrayList<>();


    /**
     * 普通对话
     * @param message
     * @return
     */
    public String chat(String message) {
        // 简单的单轮对话
        return openAiChatModel
                .call(new Prompt(message))
                .getResult().getOutput().getText();
    }

    /**
     * 使用上下文对话
     * @param message
     * @param context
     * @return
     */
    public String chatWithContext(String message, String context) {
        // 带上下文的对话
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("""
            你是一个专业的AI助手。请根据以下上下文回答问题：
            {context}
            """);

        Message systemMessage = systemPromptTemplate.createMessage(
                Map.of("context", context)
        );

        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return openAiChatModel
                .call(prompt)
                .getResult().getOutput().getText();
    }

    /**
     * 多轮对话
     * @param message
     * @return
     */
    public String multiTurnChat(String message) {
        // 添加用户消息到历史
        conversationHistory.add(new UserMessage(message));
        // 多轮对话
        Prompt prompt = new Prompt(conversationHistory);
        String aiResponse = openAiChatModel.call(prompt).getResult().getOutput().getText();

        // 添加AI回复到历史
        conversationHistory.add(new MyAssistantMessage(aiResponse));
        return aiResponse;
    }


    /**
     * 流式对话
     * @param message
     * @return
     */
    public Flux<String> chatWithStream(String message) {
        return openAiChatModel.stream(message);
    }

}


/**
 * 自定义的AssistantMessage，用于在多轮对话中，将AI的回复添加到历史记录中
 */
class MyAssistantMessage extends AssistantMessage {
    public MyAssistantMessage(String content) {
        super(content);
    }
}