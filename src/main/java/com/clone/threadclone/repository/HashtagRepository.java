package com.clone.threadclone.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clone.threadclone.model.Hashtag;

public interface HashtagRepository extends CrudRepository<Hashtag, Long> {

    List<Hashtag> findByNameIn(List<String> name);

    boolean existsByName(String name);

    Hashtag findByName(String hashtagName);
}
