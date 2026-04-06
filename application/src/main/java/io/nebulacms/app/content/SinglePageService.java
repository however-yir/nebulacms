package io.nebulacms.app.content;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.ListResult;

/**
 * Single page service.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface SinglePageService {

    Mono<ContentWrapper> getHeadContent(String singlePageName);

    Mono<ContentWrapper> getReleaseContent(String singlePageName);

    Mono<ContentWrapper> getContent(String snapshotName, String baseSnapshotName);

    Flux<ListedSnapshotDto> listSnapshots(String pageName);

    Mono<ListResult<ListedSinglePage>> list(SinglePageQuery listRequest);

    Mono<SinglePage> draft(SinglePageRequest pageRequest);

    Mono<SinglePage> update(SinglePageRequest pageRequest);

    Mono<SinglePage> revertToSpecifiedSnapshot(String pageName, String snapshotName);

    Mono<ContentWrapper> deleteContent(String postName, String snapshotName);
}
