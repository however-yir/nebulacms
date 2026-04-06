package io.nebulacms.app.content.permalinks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import io.nebulacms.app.core.extension.content.Tag;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.infra.ExternalUrlSupplier;
import io.nebulacms.app.infra.SystemConfigFetcher;

/**
 * Tests for {@link TagPermalinkPolicy}.
 *
 * @author guqing
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class TagPermalinkPolicyTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ExternalUrlSupplier externalUrlSupplier;

    @Mock
    private SystemConfigFetcher environmentFetcher;

    private TagPermalinkPolicy tagPermalinkPolicy;

    @BeforeEach
    void setUp() {
        tagPermalinkPolicy = new TagPermalinkPolicy(externalUrlSupplier, environmentFetcher);
    }

    @Test
    void permalink() {
        Tag tag = new Tag();
        Metadata metadata = new Metadata();
        metadata.setName("test-tag");
        tag.setMetadata(metadata);
        Tag.TagSpec tagSpec = new Tag.TagSpec();
        tagSpec.setSlug("test-slug");
        tag.setSpec(tagSpec);

        when(externalUrlSupplier.get()).thenReturn(URI.create(""));

        String permalink = tagPermalinkPolicy.permalink(tag);
        assertThat(permalink).isEqualTo("/tags/test-slug");

        when(externalUrlSupplier.get()).thenReturn(URI.create("http://example.com"));

        permalink = tagPermalinkPolicy.permalink(tag);
        assertThat(permalink).isEqualTo("http://example.com/tags/test-slug");

        tag.getSpec().setSlug("中文slug");
        permalink = tagPermalinkPolicy.permalink(tag);
        assertThat(permalink).isEqualTo("http://example.com/tags/%E4%B8%AD%E6%96%87slug");
    }
}