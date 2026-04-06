package io.nebulacms.app.core.reconciler;

import static io.nebulacms.app.extension.ExtensionUtil.addFinalizers;
import static io.nebulacms.app.extension.ExtensionUtil.removeFinalizers;
import static io.nebulacms.app.extension.MetadataUtil.nullSafeAnnotations;

import java.time.Duration;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import io.nebulacms.app.content.CategoryService;
import io.nebulacms.app.content.permalinks.CategoryPermalinkPolicy;
import io.nebulacms.app.core.extension.content.Category;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.event.post.CategoryHiddenStateChangeEvent;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.extension.index.query.Queries;
import io.nebulacms.app.infra.utils.ReactiveUtils;

/**
 * Reconciler for {@link Category}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@AllArgsConstructor
public class CategoryReconciler implements Reconciler<Reconciler.Request> {

    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;

    static final String FINALIZER_NAME = "category-protection";
    private final ExtensionClient client;
    private final CategoryPermalinkPolicy categoryPermalinkPolicy;
    private final CategoryService categoryService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Result reconcile(Request request) {
        client.fetch(Category.class, request.name())
            .ifPresent(category -> {
                if (ExtensionUtil.isDeleted(category)) {
                    if (removeFinalizers(category.getMetadata(), Set.of(FINALIZER_NAME))) {
                        refreshHiddenState(category, false);
                        updateCategoryForPost(category.getMetadata().getName());
                        client.update(category);
                    }
                    return;
                }
                addFinalizers(category.getMetadata(), Set.of(FINALIZER_NAME));

                populatePermalinkPattern(category);
                populatePermalink(category);
                checkHiddenState(category);

                client.update(category);
            });
        return Result.doNotRetry();
    }

    private void checkHiddenState(Category category) {
        final boolean hidden = categoryService.isCategoryHidden(category.getMetadata().getName())
            .blockOptional(BLOCKING_TIMEOUT)
            .orElse(false);
        refreshHiddenState(category, hidden);
    }

    /**
     * TODO move this logic to before-create/update hook in the future see {@code gh-4343}.
     */
    private void refreshHiddenState(Category category, boolean hidden) {
        category.getSpec().setHideFromList(hidden);
        if (isHiddenStateChanged(category)) {
            publishHiddenStateChangeEvent(category);
        }
        var children = category.getSpec().getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (String childName : children) {
            client.fetch(Category.class, childName)
                .ifPresent(child -> {
                    child.getSpec().setHideFromList(hidden);
                    if (isHiddenStateChanged(child)) {
                        publishHiddenStateChangeEvent(child);
                    }
                    client.update(child);
                });
        }
    }

    private void publishHiddenStateChangeEvent(Category category) {
        var hidden = category.getSpec().isHideFromList();
        nullSafeAnnotations(category).put(Category.LAST_HIDDEN_STATE_ANNO, String.valueOf(hidden));
        eventPublisher.publishEvent(new CategoryHiddenStateChangeEvent(this,
            category.getMetadata().getName(), hidden));
    }

    boolean isHiddenStateChanged(Category category) {
        var lastHiddenState = nullSafeAnnotations(category).get(Category.LAST_HIDDEN_STATE_ANNO);
        return !String.valueOf(category.getSpec().isHideFromList()).equals(lastHiddenState);
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new Category())
            .build();
    }

    void populatePermalinkPattern(Category category) {
        var annotations = nullSafeAnnotations(category);
        if (!annotations.containsKey(Constant.PERMALINK_PATTERN_ANNO)) {
            var newPattern = categoryPermalinkPolicy.pattern();
            annotations.put(Constant.PERMALINK_PATTERN_ANNO, newPattern);
        }
    }

    void populatePermalink(Category category) {
        category.getStatusOrDefault()
            .setPermalink(categoryPermalinkPolicy.permalink(category));
    }

    private void updateCategoryForPost(String categoryName) {
        var posts = client.listAll(Post.class, ListOptions.builder()
            .fieldQuery(Queries.equal("spec.categories", categoryName))
            .build(), Sort.by("metadata.creationTimestamp", "metadata.name")
        );
        for (Post post : posts) {
            var categoryNames = post.getSpec().getCategories();
            if (!CollectionUtils.isEmpty(categoryNames)) {
                categoryNames.remove(categoryName);
            }
            client.update(post);
        }
    }
}
