package io.nebulacms.app.content.comment;

import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Reply;
import io.nebulacms.app.extension.ListResult;

/**
 * An application service for {@link Reply}.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface ReplyService {

    Mono<Reply> create(String commentName, Reply reply);

    Mono<ListResult<ListedReply>> list(ReplyQuery query);

    Mono<Void> removeAllByComment(String commentName);
}
