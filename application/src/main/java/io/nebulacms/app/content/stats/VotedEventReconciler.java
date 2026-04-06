package io.nebulacms.app.content.stats;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.counter.MeterUtils;
import io.nebulacms.app.core.extension.Counter;
import io.nebulacms.app.event.post.DownvotedEvent;
import io.nebulacms.app.event.post.UpvotedEvent;
import io.nebulacms.app.event.post.VotedEvent;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.GroupVersionKind;
import io.nebulacms.app.extension.Scheme;
import io.nebulacms.app.extension.SchemeManager;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.DefaultController;
import io.nebulacms.app.extension.controller.DefaultQueue;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.extension.controller.RequestQueue;
import io.nebulacms.app.infra.InitializationPhase;

/**
 * Update counters after receiving upvote or downvote event.
 *
 * @author guqing
 * @since 2.0.0
 */
@Slf4j
@Component
public class VotedEventReconciler implements Reconciler<VotedEvent>, SmartLifecycle {
    private volatile boolean running = false;

    private final ExtensionClient client;
    private final RequestQueue<VotedEvent> votedEventQueue;
    private final Controller votedEventController;

    public VotedEventReconciler(ExtensionClient client) {
        this.client = client;
        votedEventQueue = new DefaultQueue<>(Instant::now);
        votedEventController = this.setupWith(null);
    }

    @Override
    public Result reconcile(VotedEvent votedEvent) {
        String counterName =
            MeterUtils.nameOf(votedEvent.getGroup(), votedEvent.getPlural(), votedEvent.getName());
        client.fetch(Counter.class, counterName)
            .ifPresentOrElse(counter -> {
                if (votedEvent instanceof UpvotedEvent) {
                    Integer existingVote = ObjectUtils.defaultIfNull(counter.getUpvote(), 0);
                    counter.setUpvote(existingVote + 1);
                } else if (votedEvent instanceof DownvotedEvent) {
                    Integer existingVote = ObjectUtils.defaultIfNull(counter.getDownvote(), 0);
                    counter.setDownvote(existingVote + 1);
                }
                client.update(counter);
            }, () -> {
                Counter counter = Counter.emptyCounter(counterName);
                if (votedEvent instanceof UpvotedEvent) {
                    counter.setUpvote(1);
                } else if (votedEvent instanceof DownvotedEvent) {
                    counter.setDownvote(1);
                }
                client.create(counter);
            });
        return new Result(false, null);
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return new DefaultController<>(
            this.getClass().getName(),
            this,
            votedEventQueue,
            null,
            Duration.ofMillis(300),
            Duration.ofMinutes(5));
    }

    @Override
    public void start() {
        this.votedEventController.start();
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
        this.votedEventController.dispose();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public int getPhase() {
        return InitializationPhase.CONTROLLERS.getPhase();
    }

    @Component
    @RequiredArgsConstructor
    public class VotedEventListener {
        private final SchemeManager schemeManager;

        /**
         * Add up/down vote event to queue.
         */
        @Async
        @EventListener(VotedEvent.class)
        public void onVoted(VotedEvent event) {
            var gpn = new GroupPluralName(event.getGroup(), event.getPlural(), event.getName());
            if (!checkSubject(gpn)) {
                log.debug("Skip voted event for: {}", gpn);
                return;
            }
            votedEventQueue.addImmediately(event);
        }

        private boolean checkSubject(
            GroupPluralName groupPluralName) {
            Optional<Scheme> schemeOptional = schemeManager.schemes().stream()
                .filter(scheme -> {
                    GroupVersionKind gvk = scheme.groupVersionKind();
                    return scheme.plural().equals(groupPluralName.plural())
                        && gvk.group().equals(groupPluralName.group());
                })
                .findFirst();
            return schemeOptional.map(
                    scheme -> client.fetch(scheme.groupVersionKind(), groupPluralName.name())
                        .isPresent()
                )
                .orElse(false);
        }

        record GroupPluralName(String group, String plural, String name) {
        }
    }
}
