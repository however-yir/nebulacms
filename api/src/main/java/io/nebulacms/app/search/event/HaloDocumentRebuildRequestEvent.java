package io.nebulacms.app.search.event;

import io.nebulacms.app.plugin.SharedEvent;

import org.springframework.context.ApplicationEvent;

@SharedEvent
public class HaloDocumentRebuildRequestEvent extends ApplicationEvent {

    public HaloDocumentRebuildRequestEvent(Object source) {
        super(source);
    }

}
