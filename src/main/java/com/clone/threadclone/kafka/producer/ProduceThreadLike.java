package com.clone.threadclone.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProduceThreadLike {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProduceThreadLike(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLike(String message) {
        kafkaTemplate.send("thread-like-unlike", message);
        System.out.println("🚀 Produce Thread Likes: " + message);
    }
}
