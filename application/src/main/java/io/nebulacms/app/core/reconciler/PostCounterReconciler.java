package io.nebulacms.app.core.reconciler;

import static io.nebulacms.app.extension.index.query.Queries.startsWith;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.counter.MeterUtils;
import io.nebulacms.app.core.extension.Counter;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.event.post.PostStatsChangedEvent;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;

@Component
@RequiredArgsConstructor
public class PostCounterReconciler implements Reconciler<Reconciler.Request> {

    private final ApplicationEventPublisher eventPublisher;
    private final ExtensionClient client;

    @Override
    public Result reconcile(Request request) {
        if (!isSameAsPost(request.name())) {
            return Result.doNotRetry();
        }
        client.fetch(Counter.class, request.name()).ifPresent(counter -> {
            eventPublisher.publishEvent(new PostStatsChangedEvent(this, counter));
        });
        return Result.doNotRetry();
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        var extension = new Counter();
        return builder
            .extension(extension)
            .syncAllListOptions(ListOptions.builder()
                .andQuery(startsWith("metadata.name", MeterUtils.nameOf(Post.class, "")))
                .build())
            .build();
    }

    static boolean isSameAsPost(String name) {
        return name.startsWith(MeterUtils.nameOf(Post.class, ""));
    }
}
