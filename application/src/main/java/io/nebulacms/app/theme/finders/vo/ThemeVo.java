package io.nebulacms.app.theme.finders.vo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.With;
import io.nebulacms.app.core.extension.Theme;
import io.nebulacms.app.extension.MetadataOperator;

/**
 * A value object for {@link Theme}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Value
@Builder
@ToString
public class ThemeVo implements ExtensionVoOperator {

    MetadataOperator metadata;

    Theme.ThemeSpec spec;

    @With
    JsonNode config;

    /**
     * Convert {@link Theme} to {@link ThemeVo}.
     *
     * @param theme theme extension
     * @return theme value object
     */
    public static ThemeVo from(Theme theme) {
        return ThemeVo.builder()
            .metadata(theme.getMetadata())
            .spec(theme.getSpec())
            .config(null)
            .build();
    }
}
