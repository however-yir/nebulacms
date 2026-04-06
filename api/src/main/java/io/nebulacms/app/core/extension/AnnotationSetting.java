package io.nebulacms.app.core.extension;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static io.nebulacms.app.core.extension.AnnotationSetting.KIND;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;
import io.nebulacms.app.extension.GroupKind;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GVK(group = "", version = "v1alpha1", kind = KIND,
    plural = "annotationsettings", singular = "annotationsetting")
public class AnnotationSetting extends AbstractExtension {
    public static final String TARGET_REF_LABEL = "nebulacms.io/target-ref";

    public static final String KIND = "AnnotationSetting";

    @Schema(requiredMode = REQUIRED)
    private AnnotationSettingSpec spec;

    @Data
    public static class AnnotationSettingSpec {
        @Schema(requiredMode = REQUIRED)
        private GroupKind targetRef;

        @Schema(requiredMode = REQUIRED, minLength = 1)
        private List<Object> formSchema;
    }
}
