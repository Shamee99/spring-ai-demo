package org.example.springaidemo.chart_03.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {


    @Autowired
    private VectorStore vectorStore;



    @GetMapping("/embedding")
    public void embedding() {
        List<String> texts = List.of("Hello world", "Bye world", "banner", "apple");

        // 将文本与嵌入向量绑定后存储
        List<Document> documents = texts.stream()
                .map(text -> new Document(text, Map.of("source", "demo")))
                .toList();
        vectorStore.add(documents);
    }


    // 执行搜索
    @GetMapping("/search")
    public List<String> search(@RequestParam String query) {
        SearchRequest request = SearchRequest.query(query).withTopK(5);
        List<Document> results = vectorStore.similaritySearch(request);

        // 提取文档内容（排除嵌入向量）
        List<String> res = results.stream()
                .map(Document::getContent)
                .toList();
        System.out.println("查询条件：" + query);
        System.out.println("查询结果：" + res);
        return res;
    }

}


/**
 * 定义一个简单的向量库
 */
@Configuration
class VectorConfig {

    @Bean
    public SimpleVectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
