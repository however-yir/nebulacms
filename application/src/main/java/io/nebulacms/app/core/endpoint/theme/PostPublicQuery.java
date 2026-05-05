package io.nebulacms.app.core.endpoint.theme;

import io.nebulacms.app.extension.router.SortableRequest;

import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.web.server.ServerWebExchange;

/**
 * Query parameters for post public APIs.
 *
 * @author guqing
 * @since 2.5.0
 */
public class PostPublicQuery extends SortableRequest {

    public PostPublicQuery(ServerWebExchange exchange) {
        super(exchange);
    }

    public static void buildParameters(Builder builder) {
        SortableRequest.buildParameters(builder);
    }
}
