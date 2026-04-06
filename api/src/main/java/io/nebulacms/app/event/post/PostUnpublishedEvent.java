package io.nebulacms.app.event.post;

import io.nebulacms.app.plugin.SharedEvent;

@SharedEvent
public class PostUnpublishedEvent extends PostEvent {

    public PostUnpublishedEvent(Object source, String postName) {
        super(source, postName);
    }

}
