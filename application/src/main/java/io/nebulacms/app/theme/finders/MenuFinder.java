package io.nebulacms.app.theme.finders;

import reactor.core.publisher.Mono;
import io.nebulacms.app.theme.finders.vo.MenuVo;

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
