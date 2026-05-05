package io.nebulacms.app.event.post;

import io.nebulacms.app.core.extension.content.Comment;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Comment created event.
 *
 * @author guqing
 * @since 2.9.0
 */
@Getter
public class CommentCreatedEvent extends ApplicationEvent {

    private final Comment comment;

    public CommentCreatedEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }
}
