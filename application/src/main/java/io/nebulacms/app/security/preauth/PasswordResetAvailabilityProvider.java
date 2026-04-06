package io.nebulacms.app.security.preauth;

import reactor.core.publisher.Mono;
import io.nebulacms.app.infra.properties.SecurityProperties;

/**
 * Password reset availability provider.
 *
 * @author johnniang
 * @since 2.20.0
 */
public interface PasswordResetAvailabilityProvider {

    /**
     * Check if the password reset method is available.
     *
     * @param method password reset method
     * @return true if available, false otherwise
     */
    Mono<Boolean> isAvailable(SecurityProperties.PasswordResetMethod method);

    /**
     * Check if the provider supports the name.
     *
     * @param name password reset method name
     * @return true if supports, false otherwise
     */
    boolean support(String name);

}
