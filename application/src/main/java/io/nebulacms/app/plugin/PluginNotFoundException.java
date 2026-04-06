package io.nebulacms.app.plugin;

import io.nebulacms.app.infra.exception.NotFoundException;

/**
 * Exception for plugin not found.
 *
 * @author guqing
 * @since 2.0.0
 */
public class PluginNotFoundException extends NotFoundException {
    public PluginNotFoundException(String message) {
        super(message);
    }

    public PluginNotFoundException(Throwable cause) {
        super(cause);
    }
}
