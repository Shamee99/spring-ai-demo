package org.example.springaidemo.chart_11.convert;

import org.springframework.ai.converter.StructuredOutputConverter;

import java.util.ArrayList;
import java.util.List;

public class ScoreListOutputConverter implements StructuredOutputConverter<List<ScoreItem>> {

    /** 告诉模型“请按 名字,分数 的格式返回，每行一条” */
    @Override
    public String getFormat() {
        return """
                Please return one item per line in the exact format:
                name,score
                (score is an integer 0-100, no headers, no extra text)
                """;
    }

    /** 真正的转换逻辑 */
    @Override
    public List<ScoreItem> convert(String source) {
        List<ScoreItem> list = new ArrayList<>();
        // 先清洗掉 markdown 代码块
        String cleaned = source.replaceAll("```.*", "").trim();
        for (String line : cleaned.split("\\r?\\n")) {
            if (line.isBlank()) continue;
            String[] split = line.split(",");
            if (split.length != 2) continue;
            try {
                String name = split[0].trim();
                int score = Integer.parseInt(split[1].trim());
                list.add(new ScoreItem(name, score));
            } catch (NumberFormatException ignore) {
                // 非法行直接跳过
            }
        }
        return list;
    }
}



