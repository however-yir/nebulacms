package io.nebulacms.app.theme.finders.impl;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.user.service.UserService;
import io.nebulacms.app.theme.finders.ContributorFinder;
import io.nebulacms.app.theme.finders.Finder;
import io.nebulacms.app.theme.finders.vo.ContributorVo;

/**
 * A default implementation of {@link ContributorFinder}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Finder("contributorFinder")
@RequiredArgsConstructor
public class ContributorFinderImpl implements ContributorFinder {

    private final UserService userService;

    @Override
    public Mono<ContributorVo> getContributor(String name) {
        return userService.getUserOrGhost(name).map(ContributorVo::from);
    }

    @Override
    public Flux<ContributorVo> getContributors(Collection<String> names) {
        return userService.getUsersOrGhosts(names).map(ContributorVo::from);
    }
}
