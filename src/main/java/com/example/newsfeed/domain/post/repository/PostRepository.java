package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository <Post, Long> {

    Page<Post> findAllByUser(User user, Pageable pageable);

}
