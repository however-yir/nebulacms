package io.nebulacms.app.content.permalinks;

import static org.springframework.web.util.UriUtils.encode;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.nebulacms.app.content.PostService;
import io.nebulacms.app.core.extension.content.Constant;
import io.nebulacms.app.core.extension.content.Post;
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
public class PostPermalinkPolicy implements PermalinkPolicy<Post> {

    private static final Duration BLOCKING_TIMEOUT = ReactiveUtils.DEFAULT_TIMEOUT;

    public static final String DEFAULT_CATEGORY = "default";
    private static final String DEFAULT_PERMALINK_PATTERN =
        SystemSetting.ThemeRouteRules.empty().getPost();
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("00");

    private final SystemConfigFetcher environmentFetcher;
    private final ExternalUrlSupplier externalUrlSupplier;
    private final PostService postService;

    @Override
    public String permalink(Post post) {
        Map<String, String> annotations = MetadataUtil.nullSafeAnnotations(post);
        String permalinkPattern =
            annotations.getOrDefault(Constant.PERMALINK_PATTERN_ANNO, DEFAULT_PERMALINK_PATTERN);
        return createPermalink(post, permalinkPattern);
    }

    public String pattern() {
        return environmentFetcher.fetchRouteRules()
            .map(PatternUtils::normalizePostPattern)
            .defaultIfEmpty(DEFAULT_PERMALINK_PATTERN)
            .block(BLOCKING_TIMEOUT);
    }

    private String createPermalink(Post post, String pattern) {
        Instant archiveTime = post.getSpec().getPublishTime();
        if (archiveTime == null) {
            archiveTime = post.getMetadata().getCreationTimestamp();
        }
        ZonedDateTime zonedDateTime = archiveTime.atZone(ZoneId.systemDefault());
        Properties properties = new Properties();
        properties.put("name", post.getMetadata().getName());
        properties.put("slug", encode(post.getSpec().getSlug(), StandardCharsets.UTF_8));
        properties.put("year", String.valueOf(zonedDateTime.getYear()));
        properties.put("month", NUMBER_FORMAT.format(zonedDateTime.getMonthValue()));
        properties.put("day", NUMBER_FORMAT.format(zonedDateTime.getDayOfMonth()));

        var categorySlug = postService.listCategories(post.getSpec().getCategories())
            .next()
            .blockOptional(BLOCKING_TIMEOUT)
            .map(category -> category.getSpec().getSlug())
            .orElse(DEFAULT_CATEGORY);
        properties.put("categorySlug", categorySlug);

        String simplifiedPattern = PathUtils.simplifyPathPattern(pattern);
        String permalink =
            PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(simplifiedPattern, properties);
        return externalUrlSupplier.get()
            .resolve(permalink)
            .normalize()
            .toString();
    }
}
