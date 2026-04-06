package io.nebulacms.app.notification;

import static io.nebulacms.app.extension.index.query.Queries.equal;
import static io.nebulacms.app.extension.router.selector.SelectorUtil.labelAndFieldSelectorToListOptions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.router.SortableRequest;

/**
 * Notification query object for authenticated user.
 *
 * @author guqing
 * @since 2.10.0
 */
public class UserNotificationQuery extends SortableRequest {

    private final String username;

    public UserNotificationQuery(ServerWebExchange exchange, String username) {
        super(exchange);
        this.username = username;
    }

    /**
     * Build a list options from the query object.
     */
    @Override
    public ListOptions toListOptions() {
        var listOptions =
            labelAndFieldSelectorToListOptions(getLabelSelector(), getFieldSelector());
        var builder = ListOptions.builder(listOptions);
        if (StringUtils.isNotBlank(username)) {
            builder.andQuery(equal("spec.recipient", username));
        }
        return builder.build();
    }
}
