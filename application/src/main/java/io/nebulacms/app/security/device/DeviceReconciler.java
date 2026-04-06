package io.nebulacms.app.security.device;

import static io.nebulacms.app.extension.ExtensionUtil.addFinalizers;
import static io.nebulacms.app.extension.ExtensionUtil.isDeleted;
import static io.nebulacms.app.extension.ExtensionUtil.removeFinalizers;
import static io.nebulacms.app.extension.index.query.Queries.equal;

import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.extension.Device;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.extension.router.selector.FieldSelector;
import io.nebulacms.app.infra.utils.ReactiveUtils;

@Component
@RequiredArgsConstructor
public class DeviceReconciler implements Reconciler<Reconciler.Request> {
    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;
    private static final int MAX_DEVICES = 10;
    static final String FINALIZER_NAME = "device-protection";
    private final ReactiveSessionRepository<?> sessionRepository;
    private final ExtensionClient client;

    @Override
    public Result reconcile(Request request) {
        client.fetch(Device.class, request.name())
            .ifPresent(device -> {
                if (isDeleted(device)) {
                    if (removeFinalizers(device.getMetadata(), Set.of(FINALIZER_NAME))) {
                        sessionRepository.deleteById(device.getSpec().getSessionId())
                            .block(BLOCKING_TIMEOUT);
                        client.update(device);
                    }
                    return;
                }
                if (addFinalizers(device.getMetadata(), Set.of(FINALIZER_NAME))) {
                    client.update(device);
                }
                revokeInactiveDevices(device.getSpec().getPrincipalName());
            });
        return Result.doNotRetry();
    }

    private void revokeInactiveDevices(String principalName) {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(
            equal("spec.principalName", principalName))
        );
        client.listAll(Device.class, listOptions,
                Sort.by("metadata.creationTimestamp").descending())
            .stream()
            .skip(MAX_DEVICES)
            .filter(device -> sessionRepository.findById(device.getSpec().getSessionId())
                .blockOptional(BLOCKING_TIMEOUT)
                .isEmpty()
            )
            .forEach(client::delete);
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new Device())
            .syncAllOnStart(false)
            .build();
    }
}
