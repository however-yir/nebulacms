package io.nebulacms.app.core.reconciler;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import io.nebulacms.app.core.extension.AnnotationSetting;
import io.nebulacms.app.extension.ExtensionClient;
import io.nebulacms.app.extension.GroupKind;
import io.nebulacms.app.extension.MetadataUtil;
import io.nebulacms.app.extension.controller.Controller;
import io.nebulacms.app.extension.controller.ControllerBuilder;
import io.nebulacms.app.extension.controller.Reconciler;

/**
 * Reconciler for {@link AnnotationSetting}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
@AllArgsConstructor
public class AnnotationSettingReconciler implements Reconciler<Reconciler.Request> {

    private final ExtensionClient client;

    @Override
    public Result reconcile(Request request) {
        populateDefaultLabels(request.name());
        return new Result(false, null);
    }

    private void populateDefaultLabels(String name) {
        client.fetch(AnnotationSetting.class, name).ifPresent(annotationSetting -> {
            Map<String, String> labels = MetadataUtil.nullSafeLabels(annotationSetting);
            String oldTargetRef = labels.get(AnnotationSetting.TARGET_REF_LABEL);

            GroupKind targetRef = annotationSetting.getSpec().getTargetRef();
            String targetRefLabel = targetRef.group() + "/" + targetRef.kind();
            labels.put(AnnotationSetting.TARGET_REF_LABEL, targetRefLabel);

            if (!StringUtils.equals(oldTargetRef, targetRefLabel)) {
                client.update(annotationSetting);
            }
        });
    }

    @Override
    public Controller setupWith(ControllerBuilder builder) {
        return builder
            .extension(new AnnotationSetting())
            .build();
    }
}
