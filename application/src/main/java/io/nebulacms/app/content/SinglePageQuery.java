package io.nebulacms.app.content;

import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static io.nebulacms.app.core.extension.content.Post.PostPhase.PENDING_APPROVAL;
import static io.nebulacms.app.core.extension.content.SinglePage.PUBLISHED_LABEL;
import static io.nebulacms.app.extension.index.query.Queries.contains;
import static io.nebulacms.app.extension.index.query.Queries.equal;
import static io.nebulacms.app.extension.index.query.Queries.in;
import static io.nebulacms.app.extension.index.query.Queries.or;
import static io.nebulacms.app.extension.router.QueryParamBuildUtil.sortParameter;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.router.IListRequest;
import io.nebulacms.app.extension.router.SortableRequest;

/**
 * Query parameter for {@link SinglePage} list.
 *
 * @author guqing
 * @since 2.0.0
 */
public class SinglePageQuery extends SortableRequest {

    public SinglePageQuery(ServerRequest request) {
        super(request.exchange());
    }

    @Override
    public ListOptions toListOptions() {
        var builder = ListOptions.builder(super.toListOptions());

        Optional.ofNullable(queryParams.getFirst("keyword"))
            .filter(StringUtils::isNotBlank)
            .ifPresent(keyword -> builder.andQuery(or(
                contains("spec.title", keyword),
                contains("spec.slug", keyword),
                contains("status.excerpt", keyword)
            )));

        Optional.ofNullable(queryParams.getFirst("publishPhase"))
            .filter(StringUtils::isNotBlank)
            .map(Post.PostPhase::from)
            .ifPresent(phase -> {
                if (PENDING_APPROVAL.equals(phase)) {
                    builder.andQuery(equal("status.phase", phase.name()));
                }
                var labelSelector = builder.labelSelector();
                Optional.of(phase)
                    .filter(Post.PostPhase.PUBLISHED::equals)
                    .ifPresentOrElse(
                        published -> labelSelector.eq(PUBLISHED_LABEL, Boolean.TRUE.toString()),
                        () -> labelSelector.notEq(PUBLISHED_LABEL, Boolean.TRUE.toString())
                    );
            });

        Optional.ofNullable(queryParams.getFirst("visible"))
            .filter(StringUtils::isNotBlank)
            .map(Post.VisibleEnum::from)
            .ifPresent(visible -> builder.andQuery(equal("spec.visible", visible.name())));

        Optional.ofNullable(queryParams.get("contributor"))
            .filter(contributors -> !contributors.isEmpty())
            .ifPresent(contributors -> builder.andQuery(in("status.contributors", contributors)));

        return builder.build();
    }

    @Override
    public Sort getSort() {
        var sort = super.getSort();
        var orders = sort.stream()
            .map(order -> {
                if ("creationTimestamp".equals(order.getProperty())) {
                    return order.withProperty("metadata.creationTimestamp");
                }
                if ("publishTime".equals(order.getProperty())) {
                    return order.withProperty("spec.publishTime");
                }
                return order;
            })
            .toList();
        return Sort.by(orders);
    }

    public static void buildParameters(Builder builder) {
        IListRequest.buildParameters(builder);
        builder.parameter(sortParameter())
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("contributor")
                .description("SinglePages filtered by contributor.")
                .implementationArray(String.class)
                .required(false))
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("publishPhase")
                .description("SinglePages filtered by publish phase.")
                .implementation(Post.PostPhase.class)
                .required(false))
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("visible")
                .description("SinglePages filtered by visibility.")
                .implementation(Post.VisibleEnum.class)
                .required(false))
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("keyword")
                .description("SinglePages filtered by keyword.")
                .implementation(String.class)
                .required(false));
    }
}
