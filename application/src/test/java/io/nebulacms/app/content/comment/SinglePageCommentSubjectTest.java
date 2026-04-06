package io.nebulacms.app.content.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.FakeExtension;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.Ref;

/**
 * Tests for {@link SinglePageCommentSubject}.
 *
 * @author guqing
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class SinglePageCommentSubjectTest {
    @Mock
    private ReactiveExtensionClient client;

    @InjectMocks
    private SinglePageCommentSubject singlePageCommentSubject;

    @Test
    void get() {
        when(client.fetch(eq(SinglePage.class), any()))
            .thenReturn(Mono.empty());

        SinglePage singlePage = new SinglePage();
        singlePage.setMetadata(new Metadata());
        singlePage.getMetadata().setName("fake-single-page");

        when(client.fetch(eq(SinglePage.class), eq("fake-single-page")))
            .thenReturn(Mono.just(singlePage));

        singlePageCommentSubject.get("fake-single-page")
            .as(StepVerifier::create)
            .expectNext(singlePage)
            .verifyComplete();

        singlePageCommentSubject.get("fake-single-page-2")
            .as(StepVerifier::create)
            .verifyComplete();

        verify(client, times(1)).fetch(eq(SinglePage.class), eq("fake-single-page"));
    }

    @Test
    void supports() {
        SinglePage singlePage = new SinglePage();
        singlePage.setMetadata(new Metadata());
        singlePage.getMetadata().setName("test");
        boolean supports = singlePageCommentSubject.supports(Ref.of(singlePage));
        assertThat(supports).isTrue();

        FakeExtension fakeExtension = new FakeExtension();
        fakeExtension.setMetadata(new Metadata());
        fakeExtension.getMetadata().setName("test");
        supports = singlePageCommentSubject.supports(Ref.of(fakeExtension));
        assertThat(supports).isFalse();
    }


    @Test
    void shouldSupportRefWithoutVersion() {
        var ref = new Ref();
        ref.setName("fake-post");
        ref.setGroup(Constant.GROUP);
        ref.setKind(SinglePage.KIND);
        assertTrue(singlePageCommentSubject.supports(ref));
    }

}