package io.nebulacms.app.content.comment;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.Ref;
import io.nebulacms.app.infra.ExternalLinkProcessor;

/**
 * Comment subject for post.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@RequiredArgsConstructor
public class PostCommentSubject implements CommentSubject<Post> {

    private final ReactiveExtensionClient client;
    private final ExternalLinkProcessor externalLinkProcessor;

    @Override
    public Mono<Post> get(String name) {
        return client.fetch(Post.class, name);
    }

    @Override
    public Mono<SubjectDisplay> getSubjectDisplay(String name) {
        return get(name)
            .map(post -> {
                var url = externalLinkProcessor
                    .processLink(post.getStatusOrDefault().getPermalink());
                return new SubjectDisplay(post.getSpec().getTitle(), url, "文章");
            });
    }

    @Override
    public boolean supports(Ref ref) {
        Assert.notNull(ref, "Subject ref must not be null.");
        var gvk = Post.GVK;
        return Objects.equals(gvk.group(), ref.getGroup())
            && Objects.equals(gvk.kind(), ref.getKind());
    }
}
