package io.nebulacms.app.core.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.MetadataOperator;

class PostTest {

    @Test
    void staticIsPublishedTest() {
        var test = (Function<Map<String, String>, Boolean>) (labels) -> {
            var metadata = Mockito.mock(MetadataOperator.class);
            when(metadata.getLabels()).thenReturn(labels);
            return Post.isPublished(metadata);
        };
        assertEquals(false, test.apply(Map.of()));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "false")));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "False")));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "0")));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "1")));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "T")));
        assertEquals(false, test.apply(Map.of("content.nebulacms.io/published", "")));
        assertEquals(true, test.apply(Map.of("content.nebulacms.io/published", "true")));
        assertEquals(true, test.apply(Map.of("content.nebulacms.io/published", "True")));
    }
}