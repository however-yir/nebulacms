package io.nebulacms.app.core.attachment.reconciler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.attachment.extension.Thumbnail;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;

@Slf4j
@Component
@Deprecated(forRemoval = true, since = "2.22.0")
class ThumbnailReconciler implements Reconciler<Reconciler.Request> {

    private final ExtensionClient client;

    ThumbnailReconciler(ExtensionClient client) {
        this.client = client;
    }

    @Override
    public Result reconcile(Request request) {
        client.fetch(Thumbnail.class, request.name())
            .ifPresent(thumbnail -> {
                if (ExtensionUtil.isDeleted(thumbnail)) {
                    return;
                }
                log.info("Clean up thumbnail: {}", thumbnail.getMetadata().getName());
                client.delete(thumbnail);
            });
        return null;
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new Thumbnail())
            .build();
    }

}
