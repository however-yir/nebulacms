package io.nebulacms.app.theme.finders;

import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.theme.finders.vo.ContentVo;
import io.nebulacms.app.theme.finders.vo.ListedSinglePageVo;
import io.nebulacms.app.theme.finders.vo.SinglePageVo;

/**
 * A finder for {@link SinglePage}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface SinglePageFinder {

    Mono<SinglePageVo> getByName(String pageName);

    Mono<ContentVo> content(String pageName);

    Mono<ListResult<ListedSinglePageVo>> list(@Nullable Integer page, @Nullable Integer size);

}
