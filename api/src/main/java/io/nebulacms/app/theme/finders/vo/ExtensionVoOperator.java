package io.nebulacms.app.theme.finders.vo;

import org.springframework.lang.NonNull;
import io.nebulacms.app.extension.MetadataOperator;

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
