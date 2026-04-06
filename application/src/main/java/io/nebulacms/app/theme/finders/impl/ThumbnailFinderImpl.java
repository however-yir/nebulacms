package io.nebulacms.app.theme.finders.impl;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.attachment.ThumbnailSize;
import io.nebulacms.app.core.attachment.thumbnail.ThumbnailService;
import io.nebulacms.app.theme.finders.Finder;
import io.nebulacms.app.theme.finders.ThumbnailFinder;

@Slf4j
@Finder("thumbnail")
@RequiredArgsConstructor
public class ThumbnailFinderImpl implements ThumbnailFinder {

    private final ThumbnailService thumbnailService;

    @Override
    public Mono<String> gen(String uriStr, String size) {
        return Mono.fromCallable(() -> URI.create(uriStr))
            .flatMap(uri -> thumbnailService.get(uri, ThumbnailSize.fromName(size)))
            .map(URI::toASCIIString)
            .onErrorComplete(IllegalArgumentException.class)
            .defaultIfEmpty(uriStr);
    }

}
