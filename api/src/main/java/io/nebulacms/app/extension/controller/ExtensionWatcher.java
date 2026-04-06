package io.nebulacms.app.extension.controller;

import io.nebulacms.app.extension.Extension;
import io.nebulacms.app.extension.Watcher;
import io.nebulacms.app.extension.WatcherExtensionMatchers;
import io.nebulacms.app.extension.controller.Reconciler.Request;

public class ExtensionWatcher implements Watcher {

    private final RequestQueue<Request> queue;

    private volatile boolean disposed = false;

    private Runnable disposeHook;

    private final WatcherExtensionMatchers matchers;

    public ExtensionWatcher(RequestQueue<Request> queue, WatcherExtensionMatchers matchers) {
        this.queue = queue;
        this.matchers = matchers;
    }

    @Override
    public void onAdd(Request request) {
        if (isDisposed()) {
            return;
        }
        queue.addImmediately(request);
    }

    @Override
    public void onAdd(Extension extension) {
        if (isDisposed() || !matchers.onAddMatcher().match(extension)) {
            return;
        }
        // TODO filter the event
        queue.addImmediately(new Request(extension.getMetadata().getName()));
    }

    @Override
    public void onUpdate(Extension oldExtension, Extension newExtension) {
        if (isDisposed() || !matchers.onUpdateMatcher().match(newExtension)) {
            return;
        }
        // TODO filter the event
        queue.addImmediately(new Request(newExtension.getMetadata().getName()));
    }

    @Override
    public void onDelete(Extension extension) {
        if (isDisposed() || !matchers.onDeleteMatcher().match(extension)) {
            return;
        }
        // TODO filter the event
        queue.addImmediately(new Request(extension.getMetadata().getName()));
    }

    @Override
    public void registerDisposeHook(Runnable dispose) {
        this.disposeHook = dispose;
    }

    @Override
    public void dispose() {
        disposed = true;
        if (this.disposeHook != null) {
            this.disposeHook.run();
        }
    }

    @Override
    public boolean isDisposed() {
        return this.disposed;
    }

}
