package io.nebulacms.app.theme.finders;

import java.util.Collection;
import java.util.List;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Tag;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.theme.finders.vo.TagVo;

/**
 * A finder for {@link Tag}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface TagFinder {

    Mono<TagVo> getByName(String name);

    Flux<TagVo> getByNames(Collection<String> names);

    Mono<ListResult<TagVo>> list(@Nullable Integer page, @Nullable Integer size);

    List<TagVo> convertToVo(List<Tag> tags);

    Flux<TagVo> listAll();
}
