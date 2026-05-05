package io.nebulacms.app.infra;

import io.nebulacms.app.infra.properties.HaloProperties;

import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class DefaultThemeRootGetter implements ThemeRootGetter {

    private final HaloProperties haloProps;

    public DefaultThemeRootGetter(HaloProperties haloProps) {
        this.haloProps = haloProps;
    }

    @Override
    public Path get() {
        return haloProps.getWorkDir().resolve("themes");
    }

}
