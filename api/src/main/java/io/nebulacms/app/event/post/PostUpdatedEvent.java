package io.nebulacms.app.event.post;

import io.nebulacms.app.plugin.SharedEvent;

@SharedEvent
public class PostUpdatedEvent extends PostEvent {

    public PostUpdatedEvent(Object source, String postName) {
        super(source, postName);
    }

}
