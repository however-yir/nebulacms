package io.nebulacms.app.theme.finders;

import java.util.Collection;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Category;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.theme.finders.vo.CategoryTreeVo;
import io.nebulacms.app.theme.finders.vo.CategoryVo;

/**
 * A finder for {@link Category}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface CategoryFinder {

    Mono<CategoryVo> getByName(String name);

    Flux<CategoryVo> getByNames(Collection<String> names);

    Mono<ListResult<CategoryVo>> list(@Nullable Integer page, @Nullable Integer size);

    Flux<CategoryVo> listAll();

    Flux<CategoryTreeVo> listAsTree();

    Flux<CategoryTreeVo> listAsTree(String name);

    Mono<CategoryVo> getParentByName(String name);

    Flux<CategoryVo> getBreadcrumbs(String name);
}
