package io.nebulacms.app.search.event;

import io.nebulacms.app.plugin.SharedEvent;
import io.nebulacms.app.search.HaloDocument;

import org.springframework.context.ApplicationEvent;

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
