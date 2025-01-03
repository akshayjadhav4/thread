package com.clone.threadclone.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumeThreadLikes {

    @KafkaListener(topics = "thread-like-unlike", groupId = "like-unlike-service-group")
    void listener(String message) {
        System.out.println("🚀 Consume Thread Likes: " + message);
    }
}
