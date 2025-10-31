package org.example.springaidemo.chart_11;

import org.example.springaidemo.chart_11.convert.ScoreItem;
import org.example.springaidemo.chart_11.convert.ScoreListOutputConverter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;



@RestController
public class StructuredOutput_2Controller {

    @Value("classpath:/templates/user-message.st")
    private Resource promptUserMessage;
    @Value("classpath:/templates/user-message-bean.st")
    private Resource promptBean;
    @Value("classpath:/templates/user-message-map.st")
    private Resource promptMap;
    @Value("classpath:/templates/user-message-list.st")
    private Resource promptList;

    private final ChatClient chatClient;
    public StructuredOutput_2Controller(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/sto")
    public Map<String, Object> st() {

        MapOutputConverter mapOutputConverter = new MapOutputConverter();

        PromptTemplate promptTemplate = new PromptTemplate(promptUserMessage);
        Prompt prompt = promptTemplate.create(Map.of("subject1", "程序员", "subject2", "打工人"));
        String content = chatClient.prompt(prompt).messages(new UserMessage("以集合的形式输出"))
                .call().content();
        assert content != null;
        return mapOutputConverter.convert(cleanLlmResponse(content));
    }

    @GetMapping("/sto/bean")
    public ProgrammerTalks bean() {
        BeanOutputConverter<ProgrammerTalks> converter =
                new BeanOutputConverter<>(ProgrammerTalks.class);

        Prompt prompt = new PromptTemplate(promptBean)
                .create(Map.of("format", converter.getFormat()));

        return chatClient.prompt(prompt)
                .call()
                .entity(ProgrammerTalks.class);
    }

    @GetMapping("/sto/map")
    public Map<String, Object> map() {
        MapOutputConverter converter = new MapOutputConverter();

        Prompt prompt = new SystemPromptTemplate(promptMap)
                .create(Map.of("format", converter.getFormat()));

        return chatClient.prompt(prompt)
                .call()
                .entity(Map.class);
    }

    @GetMapping("/sto/list")
    public List<String> list() {
        ListOutputConverter converter = new ListOutputConverter(new DefaultConversionService());
        Prompt prompt = new SystemPromptTemplate(promptList).create(Map.of("format", converter.getFormat()));
        return chatClient.prompt(prompt).call().entity(converter);
    }

    @GetMapping("/sto/score")
    public List<ScoreItem> score() {
        ScoreListOutputConverter converter = new ScoreListOutputConverter();

        String userText = """
            请给以下 3 位打工人年度表现打分（0-100）：
            张三、李四、王五
            {format}
            """;
        PromptTemplate pt = new SystemPromptTemplate(userText);
        Prompt prompt = pt.create(Map.of("format", converter.getFormat()));


        return chatClient.prompt(prompt)
                .call()
                .entity(converter);
    }

    private String cleanLlmResponse(String response) {
        if (response.contains("```json")) {
            response = response.substring(response.indexOf("```json") + 7);
        }
        if (response.contains("```")) {
            response = response.substring(0, response.lastIndexOf("```"));
        }
        // 尝试移除或替换导致 0xBB 错误的字符。
        // 0xBB 通常是多字节 UTF-8 字符（如中文标点）被错误截断或编码的结果。
        // 最常见的是U+FEFF（BOM）或其他非打印字符。
        // 这里提供一个激进的清理：移除所有非 ASCII 字符以外的控制字符和非法的 UTF-8 序列
        // 在没有更多信息的情况下，这是尝试解决编码问题的通用方法。
        // 对于 0xbb，它可能是 UTF-8 BOM（\uFEFF）的一部分，但通常 BOM 是 0xef 0xbb 0xbf。
        // 如果是 LLM 响应中头部出现，可以尝试去除前几个字符（如果有非打印字符）。

        // 假设是不可见的BOM或前导字符
        return response.trim()
                .replaceAll("\\p{C}", ""); // 移除所有控制字符
    }



    public record ProgrammerTalks(String programmer, List<String> talk) {}

}
