package io.nebulacms.app.security.authentication.rememberme;

import io.nebulacms.app.security.authentication.CryptoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DefaultCookieSignatureKeyResolver implements CookieSignatureKeyResolver {
    private final CryptoService cryptoService;

    @Override
    public Mono<String> resolveSigningKey() {
        return Mono.fromSupplier(cryptoService::getKeyId);
    }
}
