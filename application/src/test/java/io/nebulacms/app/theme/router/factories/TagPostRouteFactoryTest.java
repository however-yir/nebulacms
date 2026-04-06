package io.nebulacms.app.theme.router.factories;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Tag;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.extension.PageRequest;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.theme.finders.PostFinder;
import io.nebulacms.app.theme.finders.TagFinder;
import io.nebulacms.app.theme.finders.vo.TagVo;

/**
 * Tests for @link TagPostRouteFactory}.
 *
 * @author guqing
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class TagPostRouteFactoryTest extends RouteFactoryTestSuite {
    @Mock
    private ReactiveExtensionClient client;
    @Mock
    private TagFinder tagFinder;
    @Mock
    private PostFinder postFinder;

    @InjectMocks
    TagPostRouteFactory tagPostRouteFactory;

    @Test
    void create() {
        when(client.listBy(eq(Tag.class), any(), any(PageRequest.class)))
            .thenReturn(Mono.just(ListResult.emptyResult()));
        WebTestClient webTestClient = getWebTestClient(tagPostRouteFactory.create("/new-tags"));

        webTestClient.get()
            .uri("/new-tags/tag-slug-1")
            .exchange()
            .expectStatus().isNotFound();

        Tag tag = new Tag();
        tag.setMetadata(new Metadata());
        tag.getMetadata().setName("fake-tag-name");
        tag.setSpec(new Tag.TagSpec());
        tag.getSpec().setSlug("tag-slug-2");
        when(client.listBy(eq(Tag.class), any(), any(PageRequest.class)))
            .thenReturn(Mono.just(new ListResult<>(List.of(tag))));
        when(tagFinder.getByName(eq(tag.getMetadata().getName())))
            .thenReturn(Mono.just(TagVo.from(tag)));
        webTestClient.get()
            .uri("/new-tags/tag-slug-2")
            .exchange()
            .expectStatus().isOk();

        webTestClient.get()
            .uri("/new-tags/tag-slug-2/page/1")
            .exchange()
            .expectStatus().isOk();
    }
}