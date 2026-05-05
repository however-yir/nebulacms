package io.nebulacms.app.core.extension.attachment;

import static io.nebulacms.app.core.extension.attachment.PolicyTemplate.KIND;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GVK(group = Constant.GROUP, version = Constant.VERSION, kind = KIND,
    plural = "policytemplates", singular = "policytemplate")
public class PolicyTemplate extends AbstractExtension {

    public static final String KIND = "PolicyTemplate";

    private PolicyTemplateSpec spec;

    @Data
    public static class PolicyTemplateSpec {

        private String displayName;

        @Schema(requiredMode = REQUIRED)
        private String settingName;

    }

}
