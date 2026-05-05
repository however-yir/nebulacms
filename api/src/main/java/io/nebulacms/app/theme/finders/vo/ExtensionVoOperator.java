package io.nebulacms.app.theme.finders.vo;

import io.nebulacms.app.extension.MetadataOperator;

import org.springframework.lang.NonNull;

/**
 * An operator for extension value object.
 *
 * @author guqing
 * @since 2.0.0
 */
public interface ExtensionVoOperator {

    @NonNull
    MetadataOperator getMetadata();
}
