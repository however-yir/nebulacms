package io.nebulacms.app.theme.router.factories;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static io.nebulacms.app.extension.index.query.Queries.and;
import static io.nebulacms.app.extension.index.query.Queries.equal;
import static io.nebulacms.app.extension.index.query.Queries.isNull;
import static io.nebulacms.app.theme.router.PageUrlUtils.totalPage;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Tag;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.extension.PageRequestImpl;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.router.selector.FieldSelector;
import io.nebulacms.app.infra.SystemConfigFetcher;
import io.nebulacms.app.infra.SystemSetting;
import io.nebulacms.app.infra.exception.NotFoundException;
import io.nebulacms.app.infra.utils.PathUtils;
import io.nebulacms.app.theme.Constant;
import io.nebulacms.app.theme.DefaultTemplateEnum;
import io.nebulacms.app.theme.finders.PostFinder;
import io.nebulacms.app.theme.finders.TagFinder;
import io.nebulacms.app.theme.finders.vo.ListedPostVo;
import io.nebulacms.app.theme.finders.vo.TagVo;
import io.nebulacms.app.theme.router.ModelConst;
import io.nebulacms.app.theme.router.PageUrlUtils;
import io.nebulacms.app.theme.router.TitleVisibilityIdentifyCalculator;
import io.nebulacms.app.theme.router.UrlContextListResult;

/**
 * The {@link TagPostRouteFactory} for generate {@link RouterFunction} specific to the template
 * <code>tag.html</code>.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@AllArgsConstructor
public class TagPostRouteFactory implements RouteFactory {

    private final ReactiveExtensionClient client;
    private final SystemConfigFetcher environmentFetcher;
    private final TagFinder tagFinder;
    private final PostFinder postFinder;

    private final TitleVisibilityIdentifyCalculator titleVisibilityIdentifyCalculator;

    private final LocaleContextResolver localeContextResolver;

    @Override
    public RouterFunction<ServerResponse> create(String prefix) {
        return RouterFunctions
            .route(GET(PathUtils.combinePath(prefix, "/{slug}"))
                .or(GET(PathUtils.combinePath(prefix, "/{slug}/page/{page:\\d+}")))
                .and(accept(MediaType.TEXT_HTML)), handlerFunction());
    }

    private HandlerFunction<ServerResponse> handlerFunction() {
        return request -> tagBySlug(request.pathVariable("slug"))
            .flatMap(tagVo -> {
                int pageNum = pageNumInPathVariable(request);
                String path = request.path();
                var postList = postList(tagVo.getMetadata().getName(), pageNum, path)
                    .doOnNext(list -> list.forEach(postVo ->
                        postVo.getSpec().setTitle(
                            titleVisibilityIdentifyCalculator.calculateTitle(
                                postVo.getSpec().getTitle(),
                                postVo.getSpec().getVisible(),
                                localeContextResolver.resolveLocaleContext(request.exchange())
                                    .getLocale()
                            )
                        )
                    ));
                Map<String, Object> model = new HashMap<>();
                model.put("name", tagVo.getMetadata().getName());
                model.put("posts", postList);
                model.put("tag", tagVo);
                model.put(ModelConst.TEMPLATE_ID, DefaultTemplateEnum.TAG.getValue());
                model.put(
                    Constant.META_DESCRIPTION_VARIABLE_NAME,
                    tagVo.getSpec().getDescription()
                );
                return ServerResponse.ok()
                    .render(DefaultTemplateEnum.TAG.getValue(), model);
            });
    }

    private Mono<UrlContextListResult<ListedPostVo>> postList(String name, Integer page,
        String requestPath) {
        return configuredPageSize(environmentFetcher, SystemSetting.Post::getTagPageSize)
            .flatMap(pageSize -> postFinder.listByTag(page, pageSize, name))
            .map(list -> new UrlContextListResult.Builder<ListedPostVo>()
                .listResult(list)
                .nextUrl(PageUrlUtils.nextPageUrl(requestPath, totalPage(list)))
                .prevUrl(PageUrlUtils.prevPageUrl(requestPath))
                .build()
            );
    }

    private Mono<TagVo> tagBySlug(String slug) {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(
            and(
                equal("spec.slug", slug),
                isNull("metadata.deletionTimestamp")
            )
        ));
        return client.listBy(Tag.class, listOptions, PageRequestImpl.ofSize(1))
            .mapNotNull(result -> ListResult.first(result).orElse(null))
            .flatMap(tag -> tagFinder.getByName(tag.getMetadata().getName()))
            .switchIfEmpty(
                Mono.error(new NotFoundException("Tag not found with slug: " + slug)));
    }

}
