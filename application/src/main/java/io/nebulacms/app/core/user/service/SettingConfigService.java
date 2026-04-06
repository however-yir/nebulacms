package io.nebulacms.app.core.user.service;

import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.Setting;
import io.nebulacms.app.extension.ConfigMap;
import tools.jackson.databind.node.ObjectNode;

/**
 * {@link Setting} related {@link ConfigMap} service.
 *
 * @author guqing
 * @since 2.20.0
 */
public interface SettingConfigService {

    Mono<Void> upsertConfig(String configMapName, ObjectNode configJsonData);

    Mono<ObjectNode> fetchConfig(String configMapName);
}
