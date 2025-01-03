package com.clone.threadclone.service;

import com.clone.threadclone.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisLikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final LikeRepository likeRepository;

    private static final String THREAD_LIKES_KEY_PREFIX = "thread:likes:";
    private static final String REPLY_LIKES_KEY_PREFIX = "reply:likes:";

    public RedisLikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void likeThread(Long threadId) {
        String key = THREAD_LIKES_KEY_PREFIX + threadId;
        redisTemplate.opsForValue().increment(key);
    }

    public void unlikeThread(Long threadId) {
        String key = THREAD_LIKES_KEY_PREFIX + threadId;
        redisTemplate.opsForValue().decrement(key);
    }

    public Integer getThreadLikesCount(Long threadId) {
        String key = THREAD_LIKES_KEY_PREFIX + threadId;
        Object count = redisTemplate.opsForValue().get(key);
        if (count != null) {
            return Integer.parseInt(count.toString());
        } else {
            return likeRepository.countByThreadId(threadId);
        }

    }

    public void deleteThreadLikeCount(Long threadId) {
        String key = THREAD_LIKES_KEY_PREFIX + threadId;
        redisTemplate.delete(key);
    }


    public void likeReply(Long replyId) {
        String key = REPLY_LIKES_KEY_PREFIX + replyId;
        redisTemplate.opsForValue().increment(key);
    }

    public void unlikeReply(Long replyId) {
        String key = REPLY_LIKES_KEY_PREFIX + replyId;
        redisTemplate.opsForValue().decrement(key);
    }

    public Integer getReplyLikesCount(Long replyId) {
        String key = REPLY_LIKES_KEY_PREFIX + replyId;
        Object count = redisTemplate.opsForValue().get(key);
        if (count != null) {
            return Integer.parseInt(count.toString());
        } else {
            return likeRepository.countByReplyId(replyId);
        }

    }

    public void deleteReplyLikeCount(Long replyId) {
        String key = REPLY_LIKES_KEY_PREFIX + replyId;
        redisTemplate.delete(key);
    }
}
