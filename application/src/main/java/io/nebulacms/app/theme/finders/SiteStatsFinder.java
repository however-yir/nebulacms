package io.nebulacms.app.theme.finders;

import io.nebulacms.app.theme.finders.vo.SiteStatsVo;

import reactor.core.publisher.Mono;

/**
 * Site statistics finder.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface SiteStatsFinder {

    Mono<SiteStatsVo> getStats();
}
