package org.example.springaidemo.chart_06.chat_demo.controller;

import org.example.springaidemo.chart_06.chat_demo.service.ChatMemoryService;
import org.example.springaidemo.chart_06.chat_demo.service.ChatService;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatMemoryService chatMemoryService;

    public ChatController(ChatService chatService, ChatMemoryService chatMemoryService) {
        this.chatService = chatService;
        this.chatMemoryService = chatMemoryService;
    }

    @GetMapping("/simple")
    public String simpleChat(@RequestParam String message) {
        return chatService.chat(message);
    }

    @GetMapping("/with-context")
    public String chatWithContext(
            @RequestParam String message,
            @RequestParam String context) {
        return chatService.chatWithContext(message, context);
    }

    @GetMapping("/multi-turn")
    public String multiTurnChat(@RequestParam String message) {
        return chatService.multiTurnChat(message);
    }



    @GetMapping(value = "/with-stream")
    public Flux<String> chatWithStream(@RequestParam String message) {

        return chatService.chatWithStream(message)
                .doOnNext(System.out::println)
//                .delayElements(Duration.ofMillis(500))    // 设置流速
                .doOnComplete(() -> System.out.println("Flux 对话结束"));
    }


    @GetMapping(value = "/with-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitterUTF8 chatWithSSE(@RequestParam String message) {
        SseEmitterUTF8 emitter = new SseEmitterUTF8(5000L);

        chatService.getOpenAiChatModel().stream(new Prompt(message))
                .subscribe(
                        chunk -> {
                            try {
                                emitter.send(chunk.getResult().getOutput().getContent());
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );

        return emitter;
    }


    @GetMapping(value = "/with-memory")
    public Flux<String> chatWithMemoryStream(@RequestParam String requestId, @RequestParam String message) {
        return chatMemoryService.chatWithMemoryStream(requestId, message);
    }

}

class SseEmitterUTF8 extends SseEmitter {
    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType( new MediaType("text", "event-stream", StandardCharsets.UTF_8));
    }


    public SseEmitterUTF8(Long timeout) {
        super(timeout);
    }
}