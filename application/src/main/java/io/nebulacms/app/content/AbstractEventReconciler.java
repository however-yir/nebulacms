package io.nebulacms.app.content;

import java.time.Duration;
import java.time.Instant;
import org.springframework.context.SmartLifecycle;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.DefaultController;
import io.nebulacms.app.extension.controller.DefaultQueue;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.extension.controller.RequestQueue;
import io.nebulacms.app.infra.InitializationPhase;

/**
 * An abstract class for reconciling events.
 *
 * @author guqing
 * @since 2.15.0
 */
public abstract class AbstractEventReconciler<E> implements Reconciler<E>, SmartLifecycle {
    protected final RequestQueue<E> queue;

    protected final Controller controller;

    protected volatile boolean running = false;

    private final String controllerName;

    protected AbstractEventReconciler(String controllerName) {
        this.controllerName = controllerName;
        this.queue = new DefaultQueue<>(Instant::now);
        this.controller = this.setupWith(null);
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return new DefaultController<>(
            controllerName,
            this,
            queue,
            null,
            Duration.ofMillis(100),
            Duration.ofMinutes(10)
        );
    }

    @Override
    public void start() {
        controller.start();
        running = true;
    }

    @Override
    public void stop() {
        running = false;
        controller.dispose();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return InitializationPhase.CONTROLLERS.getPhase();
    }
}
