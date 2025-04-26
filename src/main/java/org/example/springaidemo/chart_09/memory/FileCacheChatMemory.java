package org.example.springaidemo.chart_09.memory;


import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileCacheChatMemory implements ChatMemory {

    private final Path basePath;
    private final Map<String, List<Message>> fileCacheMemory = new ConcurrentHashMap<>();

    public FileCacheChatMemory(String basePathParam) {
        this.basePath = Paths.get(basePathParam);
        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create base directory for chat history: " + this.basePath, e);
        }
    }

    private Path getFilePath(String conversationId) {
        return basePath.resolve(conversationId + ".txt");
    }


    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> retMessages = fileCacheMemory.computeIfAbsent(conversationId, k -> {
            List<Message> existingMessages = get(conversationId, Integer.MAX_VALUE);
            return new ArrayList<>(existingMessages);
        });
        retMessages.addAll(messages);
        Path filePath = getFilePath(conversationId);
        messages.forEach(message -> {
            try {
                Files.writeString(filePath, message.getText() + System.lineSeparator(),
                        Files.exists(filePath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new RuntimeException("Could not write chat message to file: " + filePath, e);
            }
        });
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        if (fileCacheMemory.containsKey(conversationId)) {
            return fileCacheMemory.get(conversationId);
        }
        Path filePath = getFilePath(conversationId);
        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath);
                List<Message> messages = lines.stream()
                        .map(UserMessage::new)
                        .collect(Collectors.toList());
                fileCacheMemory.put(conversationId, messages);
                return messages;
            } catch (IOException e) {
                throw new RuntimeException("Could not read chat history from file: " + filePath, e);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void clear(String conversationId) {
        fileCacheMemory.remove(conversationId);
        Path filePath = getFilePath(conversationId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete chat history file: " + filePath, e);
        }
    }
}