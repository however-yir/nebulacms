package io.nebulacms.app.event.post;

import io.nebulacms.app.plugin.SharedEvent;

@SharedEvent
public class PostPublishedEvent extends PostEvent {

    public PostPublishedEvent(Object source, String postName) {
        super(source, postName);
    }

}
