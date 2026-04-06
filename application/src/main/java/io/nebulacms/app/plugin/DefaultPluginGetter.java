package io.nebulacms.app.plugin;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.extension.Plugin;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.infra.exception.NotFoundException;

/**
 * Default implementation of {@link PluginGetter}.
 *
 * @author guqing
 * @since 2.17.0
 */
@Component
@RequiredArgsConstructor
public class DefaultPluginGetter implements PluginGetter {
    private final ExtensionClient client;

    @Override
    public Plugin getPlugin(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Plugin name must not be blank");
        }
        return client.fetch(Plugin.class, name)
            .orElseThrow(() -> new NotFoundException("Plugin not found"));
    }
}
