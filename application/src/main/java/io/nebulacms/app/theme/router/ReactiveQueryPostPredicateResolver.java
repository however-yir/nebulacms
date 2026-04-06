package io.nebulacms.app.theme.router;

import java.util.function.Predicate;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.ListOptions;

/**
 * The reactive query post predicate resolver.
 *
 * @author guqing
 * @since 2.9.0
 */
public interface ReactiveQueryPostPredicateResolver {

    Mono<Predicate<Post>> getPredicate();

    Mono<ListOptions> getListOptions();
}
