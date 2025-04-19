package org.example.springaidemo.chart_07.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/advisor")
public class QuestionAnswerAdvisorController {

    @Autowired
    private OpenAiChatModel openAiChatModel;
    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/question_ans")
    public void question_ans() {
        Document document = new Document("你的年龄在10-20岁左右");
        List<Document> documents = new TokenTextSplitter().apply(List.of(document));
        vectorStore.add(documents);

        QuestionAnswerAdvisor advisor = new QuestionAnswerAdvisor(vectorStore);
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultAdvisors(advisor)
                .build();

        ChatClient.CallResponseSpec response = chatClient.prompt()
                .user("你几岁了").call();

        System.out.println("-----你几岁了：" + response.content());
    }
}