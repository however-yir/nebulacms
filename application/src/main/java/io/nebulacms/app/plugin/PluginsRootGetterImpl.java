package io.nebulacms.app.plugin;

import io.nebulacms.app.infra.properties.HaloProperties;

import java.nio.file.Path;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link PluginsRootGetter}.
 *
 * @author johnniang
 */
@Component
public class PluginsRootGetterImpl implements PluginsRootGetter {

    private final HaloProperties haloProperties;

    public PluginsRootGetterImpl(HaloProperties haloProperties) {
        this.haloProperties = haloProperties;
    }

    @Override
    @NonNull
    public Path get() {
        return haloProperties.getWorkDir().resolve("plugins");
    }

}
