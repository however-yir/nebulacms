package io.nebulacms.app.core.attachment.impl;

import io.nebulacms.app.core.attachment.AttachmentRootGetter;
import io.nebulacms.app.infra.properties.HaloProperties;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachmentRootGetterImpl implements AttachmentRootGetter {
    private final HaloProperties haloProp;

    @Override
    public Path get() {
        return haloProp.getWorkDir().resolve("attachments");
    }
}
