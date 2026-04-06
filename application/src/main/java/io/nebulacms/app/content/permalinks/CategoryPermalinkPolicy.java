package io.nebulacms.app.content.permalinks;

import static org.springframework.web.util.UriUtils.encode;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.nebulacms.app.core.extension.content.Category;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.extension.MetadataUtil;
import io.nebulacms.app.infra.ExternalUrlSupplier;
import io.nebulacms.app.infra.SystemConfigFetcher;
import io.nebulacms.app.infra.SystemSetting;
import io.nebulacms.app.infra.utils.PathUtils;
import io.nebulacms.app.infra.utils.ReactiveUtils;
import io.nebulacms.app.theme.utils.PatternUtils;

/**
 * @author guqing
 * @since 2.0.0
 */
@Component
@RequiredArgsConstructor
public class CategoryPermalinkPolicy implements PermalinkPolicy<Category> {
    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;
    private static final String DEFAULT_PERMALINK_PREFIX =
        SystemSetting.ThemeRouteRules.empty().getCategories();

    private final ExternalUrlSupplier externalUrlSupplier;
    private final SystemConfigFetcher environmentFetcher;

    @Override
    public String permalink(Category category) {
        Map<String, String> annotations = MetadataUtil.nullSafeAnnotations(category);
        String permalinkPrefix =
            annotations.getOrDefault(Constant.PERMALINK_PATTERN_ANNO, DEFAULT_PERMALINK_PREFIX);
        String slug = encode(category.getSpec().getSlug(), StandardCharsets.UTF_8);
        String path = PathUtils.combinePath(permalinkPrefix, slug);
        return externalUrlSupplier.get()
            .resolve(path)
            .normalize().toString();
    }

    public String pattern() {
        return environmentFetcher.fetchRouteRules()
            .map(SystemSetting.ThemeRouteRules::getCategories)
            .defaultIfEmpty(DEFAULT_PERMALINK_PREFIX)
            .map(PatternUtils::normalizePattern)
            .block(BLOCKING_TIMEOUT);
    }
}
