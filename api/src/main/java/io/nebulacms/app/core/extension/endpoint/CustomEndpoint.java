package io.nebulacms.app.core.extension.endpoint;

import io.nebulacms.app.extension.GroupVersion;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * RouterFunction provider for custom endpoints.
 *
 * @author johnniang
 */
public interface CustomEndpoint {

    RouterFunction<ServerResponse> endpoint();

    default GroupVersion groupVersion() {
        return GroupVersion.parseAPIVersion("api.console.nebulacms.io/v1alpha1");
    }

}
