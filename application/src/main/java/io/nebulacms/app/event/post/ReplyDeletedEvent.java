package io.nebulacms.app.event.post;

import io.nebulacms.app.core.extension.content.Reply;

public class ReplyDeletedEvent extends ReplyEvent {

    public ReplyDeletedEvent(Object source, Reply reply) {
        super(source, reply);
    }
}
