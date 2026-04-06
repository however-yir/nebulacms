package io.nebulacms.app.plugin;

import static org.springframework.core.ResolvableType.forClassWithGenerics;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import reactor.core.Disposable;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.plugin.event.SpringPluginStartedEvent;
import io.nebulacms.app.plugin.event.SpringPluginStoppingEvent;

public class PluginControllerManager {

    private final ConcurrentHashMap<String, Controller> controllers;

    private final ExtensionClient client;

    public PluginControllerManager(ExtensionClient client) {
        this.client = client;
        controllers = new ConcurrentHashMap<>();
    }

    @EventListener
    public void onApplicationEvent(SpringPluginStartedEvent event) {
        event.getSpringPlugin().getApplicationContext()
            .<Reconciler<Reconciler.Request>>getBeanProvider(
                forClassWithGenerics(Reconciler.class, Reconciler.Request.class))
            .orderedStream()
            .forEach(this::start);
    }

    @EventListener
    public void onApplicationEvent(SpringPluginStoppingEvent event) throws Exception {
        controllers.values()
            .forEach(Disposable::dispose);
        controllers.clear();
    }

    private void start(Reconciler<Reconciler.Request> reconciler) {
        var builder = new ControllerBuilder(reconciler, client);
        var controller = reconciler.setupWith(builder);
        controllers.put(reconciler.getClass().getName(), controller);
        controller.start();
    }

}
