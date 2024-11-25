package com.clone.threadclone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.clone.threadclone.model.Reply;

public interface ReplyRepository extends CrudRepository<Reply, Long> {

    Page<Reply> findByThreadId(Long threadId, Pageable pageable);

    Page<Reply> findByParentReplyId(Long parentReplyId, Pageable pageable);

}
