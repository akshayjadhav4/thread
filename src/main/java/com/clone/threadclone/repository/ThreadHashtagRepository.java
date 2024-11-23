package com.clone.threadclone.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clone.threadclone.model.ThreadHashtag;

public interface ThreadHashtagRepository extends CrudRepository<ThreadHashtag, Long> {

    List<ThreadHashtag> findByThreadId(Long threadId);

}
