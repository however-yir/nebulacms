package io.nebulacms.app.theme.finders.vo;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.nebulacms.app.content.comment.OwnerInfo;
import io.nebulacms.app.core.extension.content.Comment;
import io.nebulacms.app.extension.MetadataOperator;

/**
 * A value object for {@link Comment}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class CommentVo implements ExtensionVoOperator {

    @Schema(requiredMode = REQUIRED)
    private MetadataOperator metadata;

    @Schema(requiredMode = REQUIRED)
    private Comment.CommentSpec spec;

    private Comment.CommentStatus status;

    @Schema(requiredMode = REQUIRED)
    private OwnerInfo owner;

    @Schema(requiredMode = REQUIRED)
    private CommentStatsVo stats;

    /**
     * Convert {@link Comment} to {@link CommentVo}.
     *
     * @param comment comment extension
     * @return a value object for {@link Comment}
     */
    public static CommentVo from(Comment comment) {
        return new CommentVo()
            .setMetadata(comment.getMetadata())
            .setSpec(comment.getSpec())
            .setStatus(comment.getStatus());
    }
}
