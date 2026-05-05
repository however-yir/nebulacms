package io.nebulacms.app.content.permalinks;

import io.nebulacms.app.extension.AbstractExtension;

import org.springframework.util.PropertyPlaceholderHelper;

/**
 * @author guqing
 * @since 2.0.0
 */
public interface PermalinkPolicy<T extends AbstractExtension> {

    PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER =
        new PropertyPlaceholderHelper("{", "}");

    String permalink(T extension);
}
