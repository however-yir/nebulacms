package io.nebulacms.app.content.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.nebulacms.app.content.TestPost;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.FakeExtension;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.Ref;

/**
 * Tests for {@link PostCommentSubject}.
 *
 * @author guqing
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class PostCommentSubjectTest {
    @Mock
    private ReactiveExtensionClient client;

    @InjectMocks
    private PostCommentSubject postCommentSubject;

    @Test
    void get() {
        when(client.fetch(eq(Post.class), any()))
            .thenReturn(Mono.empty());
        when(client.fetch(eq(Post.class), eq("fake-post")))
            .thenReturn(Mono.just(TestPost.postV1()));

        postCommentSubject.get("fake-post")
            .as(StepVerifier::create)
            .expectNext(TestPost.postV1())
            .verifyComplete();

        postCommentSubject.get("fake-post2")
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    void supports() {
        Post post = new Post();
        post.setMetadata(new Metadata());
        post.getMetadata().setName("test");
        boolean supports = postCommentSubject.supports(Ref.of(post));
        assertThat(supports).isTrue();

        FakeExtension fakeExtension = new FakeExtension();
        fakeExtension.setMetadata(new Metadata());
        fakeExtension.getMetadata().setName("test");
        supports = postCommentSubject.supports(Ref.of(fakeExtension));
        assertThat(supports).isFalse();
    }

    @Test
    void shouldSupportRefWithoutVersion() {
        var ref = new Ref();
        ref.setName("fake-post");
        ref.setGroup(Constant.GROUP);
        ref.setKind(Post.KIND);
        assertTrue(postCommentSubject.supports(ref));
    }
}