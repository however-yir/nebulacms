package io.nebulacms.app.content;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.Ref;

/**
 * Post and content data for creating and updating post.
 *
 * @author guqing
 * @since 2.0.0
 */
public record PostRequest(@Schema(requiredMode = REQUIRED) @NonNull Post post,
                          @Schema(requiredMode = REQUIRED) @NonNull ContentUpdateParam content) {

    public ContentRequest contentRequest() {
        Ref subjectRef = Ref.of(post);
        return new ContentRequest(subjectRef, post.getSpec().getHeadSnapshot(), content.version(),
            content.raw(), content.content(), content.rawType());
    }

}
