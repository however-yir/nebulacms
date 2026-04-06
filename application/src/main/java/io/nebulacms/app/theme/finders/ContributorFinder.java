package io.nebulacms.app.theme.finders;

import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.User;
import io.nebulacms.app.theme.finders.vo.ContributorVo;

/**
 * A finder for {@link User}.
 */
public interface ContributorFinder {

    Mono<ContributorVo> getContributor(String name);

    Flux<ContributorVo> getContributors(Collection<String> names);

}
