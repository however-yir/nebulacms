package io.nebulacms.app.search.post;

import static io.nebulacms.app.search.post.PostHaloDocumentsProvider.POST_DOCUMENT_TYPE;
import static io.nebulacms.app.search.post.PostHaloDocumentsProvider.convert;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.nebulacms.app.content.PostService;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.event.post.PostDeletedEvent;
import io.nebulacms.app.event.post.PostUpdatedEvent;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.search.event.HaloDocumentAddRequestEvent;
import io.nebulacms.app.search.event.HaloDocumentDeleteRequestEvent;

@Component
public class PostEventsListener {

    private final ApplicationEventPublisher publisher;

    private final PostService postService;

    private final ReactiveExtensionClient client;

    public PostEventsListener(
        ApplicationEventPublisher publisher,
        PostService postService,
        ReactiveExtensionClient client) {
        this.publisher = publisher;
        this.postService = postService;
        this.client = client;
    }

    @EventListener
    Mono<Void> onApplicationEvent(PostUpdatedEvent event) {
        return addOrUpdateOrDelete(event.getName());
    }

    @EventListener
    void onApplicationEvent(PostDeletedEvent event) {
        delete(event.getName());
    }

    private Mono<Void> addOrUpdateOrDelete(String postName) {
        return client.fetch(Post.class, postName)
            .flatMap(post -> {
                if (ExtensionUtil.isDeleted(post)) {
                    // if the post is deleted permanently, delete it.
                    return Mono.fromRunnable(() -> delete(postName));
                }
                // convert the post into halo document and add it to the search engine.
                return postService.getReleaseContent(post)
                    .map(content -> convert(post, content))
                    .doOnNext(haloDoc -> publisher.publishEvent(
                        new HaloDocumentAddRequestEvent(this, List.of(haloDoc))
                    ));
            })
            .then();
    }

    private void delete(String postName) {
        publisher.publishEvent(
            new HaloDocumentDeleteRequestEvent(this, List.of(POST_DOCUMENT_TYPE + '-' + postName))
        );
    }

}
