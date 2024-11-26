package com.clone.threadclone.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.clone.threadclone.model.Like;

public interface LikeRepository extends CrudRepository<Like, Long> {

    boolean existsByUserIdAndThreadId(Long id, Long threadId);

    int countByThreadId(Long threadId);

    Optional<Like> findByUserIdAndThreadId(Long id, Long threadId);

    boolean existsByUserIdAndReplyId(Long id, Long replyId);

    int countByReplyId(Long replyId);

    Optional<Like> findByUserIdAndReplyId(Long id, Long replyId);

    Page<Like> findAllByThreadId(Long threadId, Pageable pageable);

    Page<Like> findAllByReplyId(Long replyId, Pageable pageable);

}
