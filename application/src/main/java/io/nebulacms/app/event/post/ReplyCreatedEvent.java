package io.nebulacms.app.event.post;

import io.nebulacms.app.core.extension.content.Reply;

/**
 * Reply created event.
 *
 * @author guqing
 * @since 2.9.0
 */
public class ReplyCreatedEvent extends ReplyEvent {

    public ReplyCreatedEvent(Object source, Reply reply) {
        super(source, reply);
    }
}
