package io.nebulacms.app.search.event;

import org.springframework.context.ApplicationEvent;
import io.nebulacms.app.plugin.SharedEvent;
import io.nebulacms.app.search.HaloDocument;

@SharedEvent
public class HaloDocumentAddRequestEvent extends ApplicationEvent {

    private final Iterable<HaloDocument> documents;

    public HaloDocumentAddRequestEvent(Object source, Iterable<HaloDocument> documents) {
        super(source);
        this.documents = documents;
    }

    public Iterable<HaloDocument> getDocuments() {
        return documents;
    }

}
