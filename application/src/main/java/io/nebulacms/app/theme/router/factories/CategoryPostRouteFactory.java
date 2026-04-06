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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Category;
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
import io.nebulacms.app.theme.ViewNameResolver;
import io.nebulacms.app.theme.finders.PostFinder;
import io.nebulacms.app.theme.finders.vo.CategoryVo;
import io.nebulacms.app.theme.finders.vo.ListedPostVo;
import io.nebulacms.app.theme.router.ModelConst;
import io.nebulacms.app.theme.router.PageUrlUtils;
import io.nebulacms.app.theme.router.TitleVisibilityIdentifyCalculator;
import io.nebulacms.app.theme.router.UrlContextListResult;

/**
 * The {@link CategoryPostRouteFactory} for generate {@link RouterFunction} specific to the template
 * <code>category.html</code>.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@AllArgsConstructor
public class CategoryPostRouteFactory implements RouteFactory {

    private final PostFinder postFinder;

    private final SystemConfigFetcher environmentFetcher;
    private final ReactiveExtensionClient client;
    private final ViewNameResolver viewNameResolver;

    private final TitleVisibilityIdentifyCalculator titleVisibilityIdentifyCalculator;

    private final LocaleContextResolver localeContextResolver;

    @Override
    public RouterFunction<ServerResponse> create(String prefix) {
        return RouterFunctions.route(GET(PathUtils.combinePath(prefix, "/{slug}"))
            .or(GET(PathUtils.combinePath(prefix, "/{slug}/page/{page:\\d+}")))
            .and(accept(MediaType.TEXT_HTML)), handlerFunction());
    }

    HandlerFunction<ServerResponse> handlerFunction() {
        return request -> {
            String slug = request.pathVariable("slug");
            return fetchBySlug(slug)
                .flatMap(categoryVo -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put(ModelConst.TEMPLATE_ID, DefaultTemplateEnum.CATEGORY.getValue());
                    model.put("posts",
                        postListByCategoryName(categoryVo.getMetadata().getName(), request));
                    model.put("category", categoryVo);
                    model.put(
                        Constant.META_DESCRIPTION_VARIABLE_NAME,
                        categoryVo.getSpec().getDescription()
                    );
                    String template = categoryVo.getSpec().getTemplate();
                    return viewNameResolver.resolveViewNameOrDefault(request, template,
                            DefaultTemplateEnum.CATEGORY.getValue())
                        .flatMap(viewName -> ServerResponse.ok().render(viewName, model));
                })
                .switchIfEmpty(
                    Mono.error(new NotFoundException("Category not found with slug: " + slug)));
        };
    }

    Mono<CategoryVo> fetchBySlug(String slug) {
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(
            and(
                equal("spec.slug", slug),
                isNull("metadata.deletionTimestamp")
            )
        ));
        return client.listBy(Category.class, listOptions, PageRequestImpl.ofSize(1))
            .mapNotNull(result -> ListResult.first(result)
                .map(CategoryVo::from)
                .orElse(null)
            );
    }

    private Mono<UrlContextListResult<ListedPostVo>> postListByCategoryName(String name,
        ServerRequest request) {
        String path = request.path();
        int pageNum = pageNumInPathVariable(request);
        return configuredPageSize(environmentFetcher, SystemSetting.Post::getCategoryPageSize)
            .flatMap(pageSize -> postFinder.listByCategory(pageNum, pageSize, name))
            .doOnNext(list -> list.forEach(postVo -> postVo.getSpec().setTitle(
                    titleVisibilityIdentifyCalculator.calculateTitle(
                        postVo.getSpec().getTitle(),
                        postVo.getSpec().getVisible(),
                        localeContextResolver.resolveLocaleContext(request.exchange())
                            .getLocale()
                    )
                )
            ))
            .map(list -> new UrlContextListResult.Builder<ListedPostVo>()
                .listResult(list)
                .nextUrl(PageUrlUtils.nextPageUrl(path, totalPage(list)))
                .prevUrl(PageUrlUtils.prevPageUrl(path))
                .build()
            );
    }
}
