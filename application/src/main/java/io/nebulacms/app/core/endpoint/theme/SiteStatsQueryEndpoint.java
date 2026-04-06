package io.nebulacms.app.core.endpoint.theme;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;

import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.endpoint.CustomEndpoint;
import io.nebulacms.app.extension.GroupVersion;
import io.nebulacms.app.theme.finders.SiteStatsFinder;
import io.nebulacms.app.theme.finders.vo.SiteStatsVo;

/**
 * Endpoint for site stats query APIs.
 *
 * @author guqing
 * @since 2.5.0
 */
@Component
@RequiredArgsConstructor
public class SiteStatsQueryEndpoint implements CustomEndpoint {

    private final SiteStatsFinder siteStatsFinder;

    @Override
    public RouterFunction<ServerResponse> endpoint() {
        var tag = "SystemV1alpha1Public";
        return SpringdocRouteBuilder.route()
            .GET("stats/-", this::getStats,
                builder -> builder.operationId("queryStats")
                    .description("Gets site stats")
                    .tag(tag)
                    .response(responseBuilder()
                        .implementation(SiteStatsVo.class)
                    )
            )
            .build();
    }

    private Mono<ServerResponse> getStats(ServerRequest request) {
        return siteStatsFinder.getStats()
            .flatMap(result -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(result)
            );
    }

    @Override
    public GroupVersion groupVersion() {
        return new GroupVersion("api.nebulacms.io", "v1alpha1");
    }
}
