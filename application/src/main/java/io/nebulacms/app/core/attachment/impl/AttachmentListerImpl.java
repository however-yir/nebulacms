package io.nebulacms.app.core.attachment.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.attachment.AttachmentLister;
import io.nebulacms.app.core.attachment.SearchRequest;
import io.nebulacms.app.core.extension.attachment.Attachment;
import io.nebulacms.app.core.extension.attachment.Group;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.extension.ReactiveExtensionClient;

@Component
@RequiredArgsConstructor
public class AttachmentListerImpl implements AttachmentLister {
    private final ReactiveExtensionClient client;

    @Override
    public Mono<ListResult<Attachment>> listBy(SearchRequest searchRequest) {
        var groupListOptions = ListOptions.builder()
            .labelSelector()
            .exists(Group.HIDDEN_LABEL)
            .end()
            .build();
        return client.listAll(Group.class, groupListOptions, Sort.unsorted())
            .map(group -> group.getMetadata().getName())
            .collectList()
            .defaultIfEmpty(List.of())
            .flatMap(hiddenGroups -> client.listBy(Attachment.class,
                searchRequest.toListOptions(hiddenGroups),
                searchRequest.toPageRequest()
            ));
    }
}
