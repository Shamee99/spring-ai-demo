package org.example.springaidemo.chart_03.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<String> texts = List.of("问题：小锐云服pro白屏，闪退。解决方案：卸载重装。因为客户异常退出后，db损坏。",
                "问题：客户反馈打开数据漫游报错。解决方案：pro云端服务问题。",
                "问题：巡检进度条一直为0。解决方案：未知",
                "问题：软件诊断一直无法结束。解决方案：pro云端问题");

        // 将文本与嵌入向量绑定后存储
        List<Document> documents = texts.stream()
                .map(text -> new Document(text, Map.of("source", "demo")))
                .toList();
        vectorStore.add(documents);
    }


    // 执行搜索
    @GetMapping("/search")
    public List<String> search(@RequestParam String query) {
        SearchRequest request = SearchRequest.builder().query(query).topK(5).build();
        List<Document> results = vectorStore.similaritySearch(request);

        // 提取文档内容（排除嵌入向量）
        List<String> res = results.stream()
                .map(Document::getFormattedContent)
                .toList();
        System.out.println("查询条件：" + query);
        System.out.println("查询结果：" + res);
        return res;
    }

}

