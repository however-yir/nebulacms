package io.nebulacms.app.theme.finders;

import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.extension.PageRequest;
import io.nebulacms.app.extension.Ref;
import io.nebulacms.app.theme.finders.vo.CommentVo;
import io.nebulacms.app.theme.finders.vo.CommentWithReplyVo;
import io.nebulacms.app.theme.finders.vo.ReplyVo;

/**
 * comment finder.
 *
 * @author LIlGG
 */
public interface CommentPublicQueryService {
    Mono<CommentVo> getByName(String name);

    Mono<ListResult<CommentVo>> list(Ref ref, @Nullable Integer page,
        @Nullable Integer size);

    Mono<ListResult<CommentVo>> list(Ref ref, @Nullable PageRequest pageRequest);

    Mono<ListResult<CommentWithReplyVo>> convertToWithReplyVo(ListResult<CommentVo> comments,
        int replySize);

    Mono<ListResult<ReplyVo>> listReply(String commentName, @Nullable Integer page,
        @Nullable Integer size);

    Mono<ListResult<ReplyVo>> listReply(String commentName, PageRequest pageRequest);
}
