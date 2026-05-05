package io.nebulacms.app.theme.finders;

import io.nebulacms.app.theme.finders.vo.MenuVo;

import reactor.core.publisher.Mono;

/**
 * A finder for {@link io.nebulacms.app.core.extension.Menu}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface MenuFinder {

    Mono<MenuVo> getByName(String name);

    Mono<MenuVo> getPrimary();
}
