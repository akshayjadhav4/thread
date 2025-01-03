package com.clone.threadclone.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerLikes {

    @KafkaListener(topics = "thread-like-unlike", groupId = "thread-like-unlike-service-group")
    void threadLikesListener(String message) {
        System.out.println("🚀 Consume Thread Likes: " + message);
    }

    @KafkaListener(topics = "reply-like-unlike", groupId = "reply-like-unlike-service-group")
    void replyLikesListener(String message) {
        System.out.println("🚀 Consume Reply Likes: " + message);
    }
}
