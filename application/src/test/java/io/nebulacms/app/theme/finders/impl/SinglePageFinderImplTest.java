package io.nebulacms.app.theme.finders.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.core.extension.content.SinglePage;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.theme.finders.SinglePageConversionService;
import io.nebulacms.app.theme.finders.vo.SinglePageVo;

/**
 * Tests for {@link SinglePageFinderImpl}.
 *
 * @author guqing
 * @since 2.0.1
 */
@ExtendWith(MockitoExtension.class)
class SinglePageFinderImplTest {

    @Mock
    private ReactiveExtensionClient client;

    @Mock
    private SinglePageConversionService singlePageConversionService;

    @InjectMocks
    private SinglePageFinderImpl singlePageFinder;

    @Test
    void getByName() {
        // fix gh-2992
        String fakePageName = "fake-page";
        SinglePage singlePage = new SinglePage();
        singlePage.setMetadata(new Metadata());
        singlePage.getMetadata().setName(fakePageName);
        singlePage.getMetadata().setLabels(Map.of(SinglePage.PUBLISHED_LABEL, "true"));
        singlePage.setSpec(new SinglePage.SinglePageSpec());
        singlePage.getSpec().setOwner("fake-owner");
        singlePage.getSpec().setReleaseSnapshot("fake-release");
        singlePage.getSpec().setPublish(true);
        singlePage.getSpec().setDeleted(false);
        singlePage.getSpec().setVisible(Post.VisibleEnum.PUBLIC);
        singlePage.setStatus(new SinglePage.SinglePageStatus());
        when(client.get(eq(SinglePage.class), eq(fakePageName)))
            .thenReturn(Mono.just(singlePage));

        when(singlePageConversionService.convertToVo(eq(singlePage)))
            .thenReturn(Mono.just(mock(SinglePageVo.class)));

        singlePageFinder.getByName(fakePageName)
            .as(StepVerifier::create)
            .consumeNextWith(page -> assertThat(page).isNotNull())
            .verifyComplete();

        verify(client).get(SinglePage.class, fakePageName);
    }
}