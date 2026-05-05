package io.nebulacms.app.core.attachment;

import io.nebulacms.app.core.extension.attachment.Attachment;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when an attachment is created, updated, or deleted.
 *
 * @author johnniang
 */
public class AttachmentChangedEvent extends ApplicationEvent {

    @Getter
    private final Attachment attachment;

    public AttachmentChangedEvent(Object source, Attachment attachment) {
        super(source);
        this.attachment = attachment;
    }

}
