package org.example.repository;


import org.example.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Post> all() {
        return posts.values().stream()
                .filter(post -> !post.isRemoved()).collect(java.util.stream.Collectors.toList());
    }

    public List<Post> removed() {
        return posts.values().stream()
                .filter(post -> post.isRemoved()).collect(java.util.stream.Collectors.toList());
    }

    public Post getById(long id) {
        if (posts.containsKey(id) && !posts.get(id).isRemoved()) {
            return posts.get(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id does not exist");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id does not exist");
        }

        return post;
    }

    public void removeById(long id) {
        Post postToRemove = posts.get(id);
        if (postToRemove != null) {
            postToRemove.setRemoved(true);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id does not exist");
        }
    }
}

