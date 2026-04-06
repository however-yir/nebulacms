package io.nebulacms.app.security.preauth;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.nebulacms.app.infra.properties.SecurityProperties;

/**
 * Email password reset availability provider.
 *
 * @author johnniang
 * @since 2.20.0
 */
@Component
public class EmailPasswordResetAvailabilityProvider implements PasswordResetAvailabilityProvider {

    @Override
    public Mono<Boolean> isAvailable(SecurityProperties.PasswordResetMethod method) {
        // TODO Check the email notifier is available in the future
        return Mono.just(true);
    }

    @Override
    public boolean support(String name) {
        return "email".equals(name);
    }
}
