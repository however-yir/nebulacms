package io.nebulacms.app.core.attachment;

import io.nebulacms.app.core.extension.attachment.Attachment;
import io.nebulacms.app.extension.ListResult;

import reactor.core.publisher.Mono;

public interface AttachmentLister {

    Mono<ListResult<Attachment>> listBy(SearchRequest searchRequest);
}
