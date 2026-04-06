package io.nebulacms.app.content;

import static io.nebulacms.app.extension.index.query.Queries.equal;

import java.time.Duration;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.event.post.CategoryHiddenStateChangeEvent;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.router.selector.FieldSelector;
import io.nebulacms.app.infra.ReactiveExtensionPaginatedOperator;
import io.nebulacms.app.infra.utils.ReactiveUtils;

/**
 * Synchronize the {@link Post.PostStatus#getHideFromList()} state of the post with the category.
 *
 * @author guqing
 * @since 2.17.0
 */
@Component
public class PostHideFromListStateUpdater
    extends AbstractEventReconciler<CategoryHiddenStateChangeEvent> {
    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;
    private final ReactiveExtensionPaginatedOperator reactiveExtensionPaginatedOperator;
    private final ReactiveExtensionClient client;

    protected PostHideFromListStateUpdater(ReactiveExtensionClient client,
        ReactiveExtensionPaginatedOperator reactiveExtensionPaginatedOperator) {
        super(PostHideFromListStateUpdater.class.getName());
        this.reactiveExtensionPaginatedOperator = reactiveExtensionPaginatedOperator;
        this.client = client;
    }

    @Override
    public Result reconcile(CategoryHiddenStateChangeEvent request) {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(
            equal("spec.categories", request.getCategoryName())
        ));

        reactiveExtensionPaginatedOperator.list(Post.class, listOptions)
            .flatMap(post -> {
                post.getStatusOrDefault().setHideFromList(request.isHidden());
                return client.update(post);
            })
            .then()
            .block(BLOCKING_TIMEOUT);
        return Result.doNotRetry();
    }

    @EventListener(CategoryHiddenStateChangeEvent.class)
    public void onApplicationEvent(@NonNull CategoryHiddenStateChangeEvent event) {
        this.queue.addImmediately(event);
    }
}
