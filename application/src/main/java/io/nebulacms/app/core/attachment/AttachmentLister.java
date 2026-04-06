package io.nebulacms.app.core.attachment;

import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.attachment.Attachment;
import io.nebulacms.app.extension.ListResult;

public interface AttachmentLister {

    Mono<ListResult<Attachment>> listBy(SearchRequest searchRequest);
}
