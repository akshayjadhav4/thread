package com.clone.threadclone.kafka.producer;

import com.clone.threadclone.service.RedisLikeService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerLikes {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private  final RedisLikeService redisLikeService;

    public ProducerLikes(KafkaTemplate<String, String> kafkaTemplate, RedisLikeService redisLikeService) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisLikeService = redisLikeService;
    }

    public void sendThreadLike(Long threadId, Long userId) {
        try {
            redisLikeService.likeThread(threadId);
            String message = String.format("{\"action\": \"like\", \"threadId\": %d, \"userId\": %d}", threadId, userId);
            kafkaTemplate.send("thread-like-unlike", message);
        } catch (Exception ignored) {

        }
    }

    public void sendThreadUnlike(Long threadId, Long userId) {
        try {
            redisLikeService.unlikeThread(threadId);
            String message = String.format("{\"action\": \"unlike\", \"threadId\": %d, \"userId\": %d}", threadId, userId);
            kafkaTemplate.send("thread-like-unlike", message);
        } catch (Exception ignored) {
        }
    }

    public void sendReplyLike(Long replyId, Long userId) {
        try {
            redisLikeService.likeReply(replyId);
            String message = String.format("{\"action\": \"like\", \"replyId\": %d, \"userId\": %d}", replyId, userId);
            kafkaTemplate.send("reply-like-unlike", message);
        } catch (Exception ignored) {

        }
    }

    public void sendReplyUnlike(Long replyId, Long userId) {
        try {
            redisLikeService.unlikeReply(replyId);
            String message = String.format("{\"action\": \"unlike\", \"replyId\": %d, \"userId\": %d}", replyId, userId);
            kafkaTemplate.send("reply-like-unlike", message);
        } catch (Exception ignored) {

        }
    }
}
