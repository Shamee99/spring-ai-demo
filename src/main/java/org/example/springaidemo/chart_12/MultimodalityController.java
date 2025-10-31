package org.example.springaidemo.chart_12;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultimodalityController {

    private final ChatClient chatClient;
    public MultimodalityController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/multimodality")
    public String multimodality() {
        var imageResource = new ClassPathResource("/img/test.png");
        String content = chatClient.prompt().messages(new UserMessage("分析下这个图片的内容",
                        new Media(MimeTypeUtils.IMAGE_PNG, imageResource)))
                .call().content();
        System.out.println(content);
        return content;
    }



    @GetMapping("/multimodality/diff")
    public String multimodalityDiff() {
        var imageResource = new ClassPathResource("/img/test.png");
        var imageResource2 = new ClassPathResource("/img/test2.png");
        String content = chatClient.prompt().messages(new UserMessage("对比这两张图片的不同之处",
                        new Media(MimeTypeUtils.IMAGE_PNG, imageResource),
                        new Media(MimeTypeUtils.IMAGE_PNG, imageResource2)))
                .call().content();
        System.out.println(content);
        return content;
    }


}
