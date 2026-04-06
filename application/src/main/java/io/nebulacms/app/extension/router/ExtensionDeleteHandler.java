package io.nebulacms.app.extension.router;

import static io.nebulacms.app.extension.router.ExtensionRouterFunctionFactory.PathPatternGenerator.buildExtensionPathPattern;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.Scheme;
import io.nebulacms.app.extension.router.ExtensionRouterFunctionFactory.DeleteHandler;

class ExtensionDeleteHandler implements DeleteHandler {

    private final Scheme scheme;

    private final ReactiveExtensionClient client;

    ExtensionDeleteHandler(Scheme scheme, ReactiveExtensionClient client) {
        this.scheme = scheme;
        this.client = client;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        var name = request.pathVariable("name");
        return client.get(scheme.type(), name)
            .flatMap(client::delete)
            .flatMap(deleted -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleted));
    }

    @Override
    public String pathPattern() {
        return buildExtensionPathPattern(scheme) + "/{name}";
    }

}
