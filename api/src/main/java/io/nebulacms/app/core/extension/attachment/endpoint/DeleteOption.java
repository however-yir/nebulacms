package io.nebulacms.app.core.extension.attachment.endpoint;

import io.nebulacms.app.core.extension.attachment.Attachment;
import io.nebulacms.app.core.extension.attachment.Policy;
import io.nebulacms.app.extension.ConfigMap;

public record DeleteOption(Attachment attachment, Policy policy, ConfigMap configMap)
    implements AttachmentHandler.DeleteContext {
}
