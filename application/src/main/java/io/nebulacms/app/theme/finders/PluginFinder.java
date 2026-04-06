package io.nebulacms.app.theme.finders;

/**
 * A finder for {@link io.nebulacms.app.core.extension.Plugin}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface PluginFinder {

    boolean available(String pluginName);

    boolean available(String pluginName, String requiresVersion);
}
