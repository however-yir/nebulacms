package io.nebulacms.app.extension.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static io.nebulacms.app.extension.GroupVersionKind.fromExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.nebulacms.app.extension.FakeExtension;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.Scheme;
import io.nebulacms.app.extension.exception.ExtensionNotFoundException;

@ExtendWith(MockitoExtension.class)
class ExtensionGetHandlerTest {

    @Mock
    ReactiveExtensionClient client;

    @Test
    void shouldBuildPathPatternCorrectly() {
        var scheme = Scheme.buildFromType(FakeExtension.class);
        var getHandler = new ExtensionGetHandler(scheme, client);
        var pathPattern = getHandler.pathPattern();
        assertEquals("/apis/fake.nebulacms.io/v1alpha1/fakes/{name}", pathPattern);
    }

    @Test
    void shouldHandleCorrectly() {
        var scheme = Scheme.buildFromType(FakeExtension.class);
        var getHandler = new ExtensionGetHandler(scheme, client);
        var serverRequest = MockServerRequest.builder()
            .pathVariable("name", "my-fake")
            .build();
        final var fake = new FakeExtension();
        when(client.get(eq(FakeExtension.class), eq("my-fake"))).thenReturn(Mono.just(fake));

        var responseMono = getHandler.handle(serverRequest);

        StepVerifier.create(responseMono)
            .consumeNextWith(response -> {
                assertEquals(HttpStatus.OK, response.statusCode());
                assertEquals(MediaType.APPLICATION_JSON, response.headers().getContentType());
                assertTrue(response instanceof EntityResponse<?>);
                assertEquals(fake, ((EntityResponse<?>) response).entity());
            })
            .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenExtensionNotFound() {
        var scheme = Scheme.buildFromType(FakeExtension.class);
        var getHandler = new ExtensionGetHandler(scheme, client);
        var serverRequest = MockServerRequest.builder()
            .pathVariable("name", "my-fake")
            .build();
        when(client.get(eq(FakeExtension.class), eq("my-fake"))).thenReturn(Mono.error(
            new ExtensionNotFoundException(fromExtension(FakeExtension.class), "my-fake")));

        Mono<ServerResponse> responseMono = getHandler.handle(serverRequest);
        StepVerifier.create(responseMono)
            .expectError(ExtensionNotFoundException.class)
            .verify();
    }
}
