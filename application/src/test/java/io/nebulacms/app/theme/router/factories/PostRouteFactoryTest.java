package io.nebulacms.app.theme.router.factories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;
import io.nebulacms.app.content.TestPost;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.GroupVersionKind;
import io.nebulacms.app.extension.MetadataUtil;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.theme.DefaultTemplateEnum;
import io.nebulacms.app.theme.ViewNameResolver;
import io.nebulacms.app.theme.finders.PostFinder;
import io.nebulacms.app.theme.finders.vo.PostVo;
import io.nebulacms.app.theme.router.DefaultQueryPostPredicateResolver;
import io.nebulacms.app.theme.router.EmptyView;
import io.nebulacms.app.theme.router.ModelConst;
import io.nebulacms.app.theme.router.ReactiveQueryPostPredicateResolver;
import io.nebulacms.app.theme.router.TitleVisibilityIdentifyCalculator;

/**
 * Tests for {@link PostRouteFactory}.
 *
 * @author guqing
 * @since 2.3.0
 */
@ExtendWith(MockitoExtension.class)
class PostRouteFactoryTest extends RouteFactoryTestSuite {

    @Mock
    private PostFinder postFinder;

    @Mock
    private ViewNameResolver viewNameResolver;

    @Mock
    private ReactiveExtensionClient client;

    @Mock
    private ReactiveQueryPostPredicateResolver predicateResolver;

    @Mock
    private LocaleContextResolver localeContextResolver;

    @Mock
    private TitleVisibilityIdentifyCalculator titleVisibilityIdentifyCalculator;

    @InjectMocks
    private PostRouteFactory postRouteFactory;

    @Test
    void shouldBeSameResultWhenParsePattenMultiply() {
        var parser = new PostRouteFactory.PatternParser("/?p={slug}");
        Assertions.assertTrue(parser.isQueryParamPattern());
        parser = new PostRouteFactory.PatternParser("/?p={slug}");
        Assertions.assertTrue(parser.isQueryParamPattern());
    }

    @Test
    void create() {
        Post post = TestPost.postV1();
        Map<String, String> labels = MetadataUtil.nullSafeLabels(post);
        labels.put(Post.PUBLISHED_LABEL, "true");
        post.getMetadata().setName("fake-name");
        post.getSpec().setDeleted(false);
        post.getSpec().setVisible(Post.VisibleEnum.PUBLIC);
        when(postFinder.getByName(eq("fake-name"))).thenReturn(Mono.just(PostVo.from(post)));

        when(client.fetch(eq(Post.class), eq("fake-name"))).thenReturn(Mono.just(post));

        when(viewNameResolver.resolveViewNameOrDefault(any(ServerRequest.class), any(), any()))
            .thenReturn(Mono.just(DefaultTemplateEnum.POST.getValue()));
        when(predicateResolver.getPredicate())
            .thenReturn(new DefaultQueryPostPredicateResolver().getPredicate());

        RouterFunction<ServerResponse> routerFunction = postRouteFactory.create("/archives/{name}");
        WebTestClient webTestClient = getWebTestClient(routerFunction);

        when(localeContextResolver.resolveLocaleContext(any()))
            .thenReturn(new SimpleLocaleContext(Locale.getDefault()));
        when(viewResolver.resolveViewName(any(), any()))
            .thenReturn(Mono.just(new EmptyView() {
                @Override
                public Mono<Void> render(Map<String, ?> model, MediaType contentType,
                    ServerWebExchange exchange) {
                    assertThat(model).containsKey(ModelConst.TEMPLATE_ID);
                    assertThat(model.get(ModelConst.TEMPLATE_ID))
                        .isEqualTo(DefaultTemplateEnum.POST.getValue());
                    assertThat(model.get("name"))
                        .isEqualTo(post.getMetadata().getName());
                    assertThat(model.get("plural")).isEqualTo("posts");
                    assertThat(model.get("post")).isNotNull();
                    assertThat(model.get("groupVersionKind"))
                        .isEqualTo(GroupVersionKind.fromExtension(Post.class));
                    return super.render(model, contentType, exchange);
                }
            }));
        webTestClient.get()
            .uri("/archives/fake-name")
            .exchange()
            .expectStatus().isOk();
    }
}
