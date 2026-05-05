package io.nebulacms.app.theme;

import io.nebulacms.app.core.extension.content.SinglePage;

import lombok.Builder;
import lombok.Data;
import org.pf4j.ExtensionPoint;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

/**
 * <p>{@link ReactiveSinglePageContentHandler} provides a way to extend the content to be
 * displayed in the theme.</p>
 *
 * @author guqing
 * @see ReactivePostContentHandler
 * @since 2.7.0
 */
public interface ReactiveSinglePageContentHandler extends ExtensionPoint {

    /**
     * <p>Methods for handling
     *     {@link io.nebulacms.app.core.extension.content.SinglePage} content.</p>
     * <p>For example, you can use this method to change the content for a better display in
     * theme-side.</p>
     *
     * @param singlePageContent content to be handled
     * @return handled content
     */
    Mono<SinglePageContentContext> handle(@NonNull SinglePageContentContext singlePageContent);

    @Data
    @Builder
    class SinglePageContentContext {
        private SinglePage singlePage;
        private String content;
        private String raw;
        private String rawType;
    }
}
