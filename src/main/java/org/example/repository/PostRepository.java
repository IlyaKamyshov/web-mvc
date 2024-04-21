package org.example.repository;


import org.example.model.Post;
import org.example.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        long postId = post.getId();

        if (postId == 0) {
            long newPostId = idCounter.getAndIncrement();
            post.setId(newPostId);
            posts.put(newPostId, post);
        }

        if (postId != 0 && !posts.containsKey(postId)) {
            posts.put(postId, post);
        } else {
            throw new NotFoundException("id already exists");
        }

        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)){
            posts.remove(id);
        }else {
            throw new NotFoundException("id does not exist");
        }
    }
}

