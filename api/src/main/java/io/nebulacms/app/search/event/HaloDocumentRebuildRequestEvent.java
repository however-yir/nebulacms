package io.nebulacms.app.search.event;

import org.springframework.context.ApplicationEvent;
import io.nebulacms.app.plugin.SharedEvent;

@SharedEvent
public class HaloDocumentRebuildRequestEvent extends ApplicationEvent {

    public HaloDocumentRebuildRequestEvent(Object source) {
        super(source);
    }

}
