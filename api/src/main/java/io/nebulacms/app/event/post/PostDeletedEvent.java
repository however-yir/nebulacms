package io.nebulacms.app.event.post;

import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.plugin.SharedEvent;

@SharedEvent
public class PostDeletedEvent extends PostEvent {

    private final Post post;

    public PostDeletedEvent(Object source, Post post) {
        super(source, post.getMetadata().getName());
        this.post = post;
    }

    /**
     * Get original post.
     *
     * @return original post.
     */
    public Post getPost() {
        return post;
    }
}
