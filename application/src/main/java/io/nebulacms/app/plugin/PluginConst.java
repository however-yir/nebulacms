package io.nebulacms.app.plugin;

/**
 * Plugin constants.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface PluginConst {
    /**
     * Plugin metadata labels key.
     */
    String PLUGIN_NAME_LABEL_NAME = "plugin.nebulacms.io/plugin-name";

    String SYSTEM_PLUGIN_NAME = "system";

    String RELOAD_ANNO = "plugin.nebulacms.io/reload";

    String REQUEST_TO_UNLOAD_LABEL = "plugin.nebulacms.io/request-to-unload";

    String PLUGIN_PATH = "plugin.nebulacms.io/plugin-path";

    String RUNTIME_MODE_ANNO = "plugin.nebulacms.io/runtime-mode";

    static String assetsRoutePrefix(String pluginName) {
        return "/plugins/" + pluginName + "/assets/";
    }

}
