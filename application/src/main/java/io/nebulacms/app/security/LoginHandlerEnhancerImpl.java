package io.nebulacms.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.user.service.UserLoginOrLogoutProcessing;
import io.nebulacms.app.security.authentication.oauth2.OAuth2LoginHandlerEnhancer;
import io.nebulacms.app.security.authentication.rememberme.RememberMeRequestCache;
import io.nebulacms.app.security.authentication.rememberme.RememberMeServices;
import io.nebulacms.app.security.authentication.rememberme.WebSessionRememberMeRequestCache;
import io.nebulacms.app.security.device.DeviceService;

/**
 * A default implementation for {@link LoginHandlerEnhancer} to handle device management and
 * remember me.
 *
 * @author guqing
 * @since 2.17.0
 */
@Component
@RequiredArgsConstructor
public class LoginHandlerEnhancerImpl implements LoginHandlerEnhancer {

    private final RememberMeServices rememberMeServices;

    private final DeviceService deviceService;

    private final RememberMeRequestCache rememberMeRequestCache =
        new WebSessionRememberMeRequestCache();

    private final OAuth2LoginHandlerEnhancer oauth2LoginHandlerEnhancer;

    private final UserLoginOrLogoutProcessing userLoginOrLogoutProcessing;

    @Override
    public Mono<Void> onLoginSuccess(ServerWebExchange exchange,
        Authentication successfulAuthentication) {
        return Mono.when(
            rememberMeServices.loginSuccess(exchange, successfulAuthentication),
            deviceService.loginSuccess(exchange, successfulAuthentication),
            rememberMeRequestCache.removeRememberMe(exchange),
            oauth2LoginHandlerEnhancer.loginSuccess(exchange, successfulAuthentication),
            userLoginOrLogoutProcessing.loginProcessing(successfulAuthentication.getName())
        );
    }

    @Override
    public Mono<Void> onLoginFailure(ServerWebExchange exchange,
        AuthenticationException exception) {
        return rememberMeServices.loginFail(exchange);
    }
}
