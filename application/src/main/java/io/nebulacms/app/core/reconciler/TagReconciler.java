package io.nebulacms.app.core.reconciler;

import static io.nebulacms.app.extension.ExtensionUtil.addFinalizers;
import static io.nebulacms.app.extension.ExtensionUtil.removeFinalizers;
import static io.nebulacms.app.extension.index.query.Queries.equal;

import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.nebulacms.app.content.permalinks.TagPermalinkPolicy;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.core.extension.content.Tag;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.MetadataUtil;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;

/**
 * Reconciler for {@link Tag}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@RequiredArgsConstructor
public class TagReconciler implements Reconciler<Reconciler.Request> {
    static final String FINALIZER_NAME = "tag-protection";
    private final ExtensionClient client;
    private final TagPermalinkPolicy tagPermalinkPolicy;

    @Override
    public Result reconcile(Request request) {
        client.fetch(Tag.class, request.name())
            .ifPresent(tag -> {
                if (ExtensionUtil.isDeleted(tag)) {
                    if (removeFinalizers(tag.getMetadata(), Set.of(FINALIZER_NAME))) {
                        client.update(tag);
                    }
                    return;
                }

                addFinalizers(tag.getMetadata(), Set.of(FINALIZER_NAME));

                Map<String, String> annotations = MetadataUtil.nullSafeAnnotations(tag);

                if (!annotations.containsKey(Constant.PERMALINK_PATTERN_ANNO)) {
                    var newPattern = tagPermalinkPolicy.pattern();
                    annotations.put(Constant.PERMALINK_PATTERN_ANNO, newPattern);
                }

                var status = tag.getStatusOrDefault();
                String permalink = tagPermalinkPolicy.permalink(tag);
                status.setPermalink(permalink);

                if (status.getPostCount() == null) {
                    status.setPostCount(0);
                }
                if (status.getVisiblePostCount() == null) {
                    status.setVisiblePostCount(0);
                }

                // Update the observed version.
                status.setObservedVersion(tag.getMetadata().getVersion() + 1);

                client.update(tag);
            });
        return Result.doNotRetry();
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new Tag())
            .syncAllListOptions(ListOptions.builder()
                .andQuery(equal(Tag.REQUIRE_SYNC_ON_STARTUP_INDEX_NAME, true))
                .build())
            .build();
    }
}
