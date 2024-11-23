package com.clone.threadclone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.clone.threadclone.model.Thread;

public interface ThreadRepository extends CrudRepository<Thread, Long>, PagingAndSortingRepository<Thread, Long> {

    Page<Thread> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT t FROM Thread t " +
            "JOIN t.threadHashtags th " +
            "JOIN th.hashtag h " +
            "WHERE h.name = :hashtagName")
    Page<Thread> findByHashtagName(@Param("hashtagName") String hashtagName, Pageable pageable);

}
