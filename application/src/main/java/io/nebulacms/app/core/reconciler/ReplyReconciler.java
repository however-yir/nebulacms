package io.nebulacms.app.core.reconciler;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static io.nebulacms.app.extension.ExtensionUtil.addFinalizers;
import static io.nebulacms.app.extension.index.query.Queries.equal;

import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import io.nebulacms.app.content.comment.ReplyNotificationSubscriptionHelper;
import io.nebulacms.app.core.extension.content.Reply;
import io.nebulacms.app.event.post.ReplyChangedEvent;
import io.nebulacms.app.event.post.ReplyCreatedEvent;
import io.nebulacms.app.event.post.ReplyDeletedEvent;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;

/**
 * Reconciler for {@link Reply}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@AllArgsConstructor
public class ReplyReconciler implements Reconciler<Reconciler.Request> {
    protected static final String FINALIZER_NAME = "reply-protection";

    private final ExtensionClient client;
    private final ApplicationEventPublisher eventPublisher;

    private final ReplyNotificationSubscriptionHelper replyNotificationSubscriptionHelper;

    @Override
    public Result reconcile(Request request) {
        client.fetch(Reply.class, request.name())
            .ifPresent(reply -> {
                if (reply.getMetadata().getDeletionTimestamp() != null) {
                    cleanUpResourcesAndRemoveFinalizer(request.name());
                    return;
                }
                if (addFinalizers(reply.getMetadata(), Set.of(FINALIZER_NAME))) {
                    replyNotificationSubscriptionHelper.subscribeNewReplyReasonForReply(reply);
                    client.update(reply);
                    eventPublisher.publishEvent(new ReplyCreatedEvent(this, reply));
                }

                if (reply.getSpec().getCreationTime() == null) {
                    reply.getSpec().setCreationTime(
                        defaultIfNull(reply.getSpec().getApprovedTime(),
                            reply.getMetadata().getCreationTimestamp()
                        )
                    );
                }

                // version + 1 is required to truly equal version
                // as a version will be incremented after the update
                reply.getStatus().setObservedVersion(reply.getMetadata().getVersion() + 1);

                client.update(reply);

                eventPublisher.publishEvent(new ReplyChangedEvent(this, reply));
            });
        return new Result(false, null);
    }

    private void cleanUpResourcesAndRemoveFinalizer(String replyName) {
        client.fetch(Reply.class, replyName).ifPresent(reply -> {
            if (reply.getMetadata().getFinalizers() != null) {
                reply.getMetadata().getFinalizers().remove(FINALIZER_NAME);
            }
            client.update(reply);

            // on reply removing
            eventPublisher.publishEvent(new ReplyDeletedEvent(this, reply));
        });
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        var extension = new Reply();
        return builder
            .extension(extension)
            .syncAllListOptions(ListOptions.builder()
                .andQuery(equal(Reply.REQUIRE_SYNC_ON_STARTUP_INDEX_NAME, true))
                .build())
            .build();
    }
}
