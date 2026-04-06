package io.nebulacms.app.core.reconciler;

import static java.util.Objects.requireNonNullElse;
import static io.nebulacms.app.extension.index.query.Queries.equal;
import static io.nebulacms.app.infra.utils.SystemConfigUtils.mergeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import io.nebulacms.app.extension.ConfigMap;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ExtensionMatcher;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.infra.SystemConfigChangedEvent;
import io.nebulacms.app.infra.SystemSetting;
import io.nebulacms.app.infra.utils.SystemConfigUtils;

@Slf4j
@Component
@RequiredArgsConstructor
class SystemConfigReconciler implements Reconciler<Reconciler.Request> {

    private final ExtensionClient client;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Result reconcile(Request request) {
        Assert.state(
            Objects.equals(SystemSetting.SYSTEM_CONFIG, request.name()),
            "Only system config reconciler is supported to reconcile system config."
        );
        client.fetch(ConfigMap.class, request.name())
            .ifPresent(configMap -> {
                if (ExtensionUtil.isDeleted(configMap)) {
                    log.warn("System config was attempted to be deleted");
                    return;
                }
                // calculate if the configMap has changed
                // and publish event if changed
                var dataSnapshot = SystemConfigUtils.getDataSnapshot(configMap);
                if (SystemConfigUtils.populateChecksum(configMap)) {
                    SystemConfigUtils.updateDataSnapshot(configMap);
                    client.update(configMap);
                    log.info("System config has been detected as changed");
                    eventPublisher.publishEvent(
                        computeChangedEvent(configMap, dataSnapshot)
                    );
                }
                // do nothing if not changed
            });
        return null;
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        ExtensionMatcher matcher = extension ->
            Objects.equals(extension.getMetadata().getName(), SystemSetting.SYSTEM_CONFIG);
        return builder.extension(new ConfigMap())
            .syncAllOnStart(true)
            .syncAllListOptions(ListOptions.builder()
                .fieldQuery(equal("metadata.name", SystemSetting.SYSTEM_CONFIG))
                .build()
            )
            .onAddMatcher(matcher)
            .onUpdateMatcher(matcher)
            .onDeleteMatcher(matcher)
            .build();
    }

    private SystemConfigChangedEvent computeChangedEvent(ConfigMap configMap,
        @Nullable Map<String, String> oldData) {
        return client.fetch(ConfigMap.class, SystemSetting.SYSTEM_CONFIG_DEFAULT)
            .map(defaultConfigMap -> {
                var defaultData =
                    requireNonNullElse(defaultConfigMap.getData(), Map.<String, String>of());
                try {
                    var mergedOldData = mergeMap(
                        defaultData, requireNonNullElse(oldData, Map.of())
                    );
                    var mergedNewData = mergeMap(
                        defaultData, requireNonNullElse(configMap.getData(), Map.of())
                    );
                    return new SystemConfigChangedEvent(this, mergedOldData, mergedNewData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElseGet(() -> new SystemConfigChangedEvent(
                this, oldData, requireNonNullElse(configMap.getData(), Map.of())
            ));
    }

}
