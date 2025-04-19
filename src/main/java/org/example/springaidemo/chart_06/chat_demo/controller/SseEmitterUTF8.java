package org.example.springaidemo.chart_06.chat_demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;

public class SseEmitterUTF8 extends SseEmitter {
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
