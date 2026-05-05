package io.nebulacms.app.theme.router.factories;

import static org.mockito.Mockito.when;

import io.nebulacms.app.theme.finders.CategoryFinder;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

/**
 * Tests for {@link CategoriesRouteFactory}.
 *
 * @author guqing
 * @since 2.0.0
 */
class CategoriesRouteFactoryTest extends RouteFactoryTestSuite {

    @Mock
    private CategoryFinder categoryFinder;

    @InjectMocks
    private CategoriesRouteFactory categoriesRouteFactory;

    @Test
    void create() {
        String prefix = "/topics";
        RouterFunction<ServerResponse> routerFunction = categoriesRouteFactory.create(prefix);
        WebTestClient webClient = getWebTestClient(routerFunction);

        when(categoryFinder.listAsTree())
            .thenReturn(Flux.empty());
        webClient.get()
            .uri(prefix)
            .exchange()
            .expectStatus().isOk();
    }
}
