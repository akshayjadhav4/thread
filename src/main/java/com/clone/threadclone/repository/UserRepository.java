package com.clone.threadclone.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clone.threadclone.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String username);

    @Modifying
    @Query(value = "INSERT INTO user_followers (user_id, follower_id) VALUES (:followedId, :followerId) ON CONFLICT DO NOTHING", nativeQuery = true)
    void followUser(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

    @Modifying
    @Query(value = "DELETE FROM user_followers WHERE user_id = :followedId AND follower_id = :followerId", nativeQuery = true)
    void unfollowUser(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

    @Query("SELECT u.followers FROM User u WHERE u.id = :userId")
    Page<User> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u.following FROM User u WHERE u.id = :userId")
    Page<User> findFollowingByUserId(@Param("userId") Long userId, Pageable pageable);
}
