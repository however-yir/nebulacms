package io.nebulacms.app.content.comment;

import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springframework.data.domain.Sort.Order.desc;
import static io.nebulacms.app.extension.index.query.Queries.contains;
import static io.nebulacms.app.extension.index.query.Queries.equal;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.server.ServerRequest;
import io.nebulacms.app.core.extension.User;
import io.nebulacms.app.core.extension.content.Comment;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.router.IListRequest;
import io.nebulacms.app.extension.router.QueryParamBuildUtil;
import io.nebulacms.app.extension.router.SortableRequest;

/**
 * Query criteria for comment list.
 *
 * @author guqing
 * @since 2.0.0
 */
public class CommentQuery extends SortableRequest {

    public CommentQuery(ServerRequest request) {
        super(request.exchange());
    }

    @Nullable
    public String getKeyword() {
        return queryParams.getFirst("keyword");
    }

    @Nullable
    public String getOwnerKind() {
        return queryParams.getFirst("ownerKind");
    }

    @Nullable
    public String getOwnerName() {
        return queryParams.getFirst("ownerName");
    }

    @Override
    public Sort getSort() {
        // set default sort by last reply time
        return super.getSort().and(Sort.by(desc("status.lastReplyTime")));
    }

    /**
     * Convert to list options.
     */
    @Override
    public ListOptions toListOptions() {
        var builder = ListOptions.builder(super.toListOptions());

        Optional.ofNullable(getKeyword())
            .filter(StringUtils::isNotBlank)
            .ifPresent(keyword -> builder.andQuery(contains("spec.raw", keyword)));

        Optional.ofNullable(getOwnerName())
            .filter(StringUtils::isNotBlank)
            .ifPresent(ownerName -> {
                var ownerKind = Optional.ofNullable(getOwnerKind())
                    .filter(StringUtils::isNotBlank)
                    .orElse(User.KIND);
                builder.andQuery(
                    equal("spec.owner", Comment.CommentOwner.ownerIdentity(ownerKind, ownerName))
                );
            });

        return builder.build();
    }

    public static void buildParameters(Builder builder) {
        IListRequest.buildParameters(builder);
        builder.parameter(QueryParamBuildUtil.sortParameter())
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("keyword")
                .description("Comments filtered by keyword.")
                .implementation(String.class))
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("ownerKind")
                .description("Commenter kind.")
                .implementation(String.class))
            .parameter(parameterBuilder()
                .in(ParameterIn.QUERY)
                .name("ownerName")
                .description("Commenter name.")
                .implementation(String.class));
    }
}
