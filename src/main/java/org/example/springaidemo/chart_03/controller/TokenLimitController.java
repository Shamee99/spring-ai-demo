//package org.example.springaidemo.chart_03.controller;
//
//import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
//import ai.djl.huggingface.tokenizers.jni.TokenizersLibrary;
//import ai.djl.modality.nlp.preprocess.Tokenizer;
//import org.springframework.ai.autoconfigure.transformers.TransformersEmbeddingModelProperties;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.prompt.ChatOptions;
//import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.transformer.splitter.TokenTextSplitter;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//public class TokenLimitController {
//
//
//    private final ChatClient chatClient;
//    public TokenLimitController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }
//
//    // 生成一个超长的测试文本（约5000 Tokens）
//    private String generateLongText() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 10; i++) {
//            sb.append("自然语言处理是人工智能的重要分支。").append(i).append(" ");
//        }
//        return sb.toString();
//    }
//
//    @GetMapping("tokens")
//    public void tokens() throws IOException {
//        String longText = generateLongText();
//        HuggingFaceTokenizer huggingFaceTokenizer = HuggingFaceTokenizer
//                .builder().optTokenizerName("my-tokenizer-to-count-size").build();
//        int tokenLen = huggingFaceTokenizer.encode(longText).getTokens().length;
//        System.out.println("原始文本Token数: " + tokenLen);
//
////        // 初始化分割器（每块最多1000 Tokens，块间重叠50 Tokens）
////        TokenTextSplitter splitter = new TokenTextSplitter();
////        List<Document> chunks = splitter.split(List.of(new Document(longText)));
////
////        // 处理每个文本块
////        StringBuilder result = new StringBuilder();
////        for (Document chunk : chunks) {
////            String summary = chatClient.call("请用一句话总结以下文本: " + chunk.getContent());
////            result.append(summary).append("\n---\n");
////        }
////
////        return "分割块数: " + chunks.size() + "\n总结结果:\n" + result;
//    }
//
//
//}
