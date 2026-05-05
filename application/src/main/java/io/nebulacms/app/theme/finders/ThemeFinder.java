package io.nebulacms.app.theme.finders;

import io.nebulacms.app.theme.finders.vo.ThemeVo;

import reactor.core.publisher.Mono;

/**
 * A finder for theme.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface ThemeFinder {

    Mono<ThemeVo> activation();

    Mono<ThemeVo> getByName(String themeName);
}
