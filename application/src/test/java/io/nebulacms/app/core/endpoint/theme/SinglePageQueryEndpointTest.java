package io.nebulacms.app.core.endpoint.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.GroupVersion;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.theme.finders.SinglePageFinder;
import io.nebulacms.app.theme.finders.vo.SinglePageVo;

/**
 * Tests for {@link SinglePageQueryEndpoint}.
 *
 * @author guqing
 * @since 2.5.0
 */
@ExtendWith(MockitoExtension.class)
class SinglePageQueryEndpointTest {

    @Mock
    private SinglePageFinder singlePageFinder;

    @InjectMocks
    private SinglePageQueryEndpoint endpoint;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(endpoint.endpoint()).build();
    }

    @Test
    void getByName() {
        SinglePageVo singlePage = SinglePageVo.builder()
            .metadata(metadata("fake-page"))
            .spec(new SinglePage.SinglePageSpec())
            .build();

        when(singlePageFinder.getByName(eq("fake-page")))
            .thenReturn(Mono.just(singlePage));

        webTestClient.get()
            .uri("/singlepages/fake-page")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.metadata.name").isEqualTo("fake-page");

        verify(singlePageFinder).getByName("fake-page");
    }

    Metadata metadata(String name) {
        Metadata metadata = new Metadata();
        metadata.setName(name);
        return metadata;
    }

    @Test
    void groupVersion() {
        GroupVersion groupVersion = endpoint.groupVersion();
        assertThat(groupVersion.toString()).isEqualTo("api.content.nebulacms.io/v1alpha1");
    }
}