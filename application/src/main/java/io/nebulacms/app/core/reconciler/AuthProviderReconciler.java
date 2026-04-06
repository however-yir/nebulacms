package io.nebulacms.app.core.reconciler;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.extension.AuthProvider;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.MetadataUtil;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;
import io.nebulacms.app.infra.utils.ReactiveUtils;
import io.nebulacms.app.security.AuthProviderService;

/**
 * Reconciler for {@link AuthProvider}.
 *
 * @author guqing
 * @since 2.4.0
 */
@Component
@RequiredArgsConstructor
public class AuthProviderReconciler implements Reconciler<Reconciler.Request> {
    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;
    private final ExtensionClient client;
    private final AuthProviderService authProviderService;

    @Override
    public Result reconcile(Request request) {
        client.fetch(AuthProvider.class, request.name())
            .ifPresent(this::handlePrivileged);
        return Result.doNotRetry();
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new AuthProvider())
            .build();
    }

    private void handlePrivileged(AuthProvider authProvider) {
        if (privileged(authProvider)) {
            authProviderService.enable(authProvider.getMetadata().getName())
                .block(BLOCKING_TIMEOUT);
        }
    }

    private boolean privileged(AuthProvider authProvider) {
        return BooleanUtils.TRUE.equals(MetadataUtil.nullSafeLabels(authProvider)
            .get(AuthProvider.PRIVILEGED_LABEL));
    }
}
