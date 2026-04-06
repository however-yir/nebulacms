package io.nebulacms.app.content.comment;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Data;
import io.nebulacms.app.core.extension.content.Reply;
import io.nebulacms.app.extension.Metadata;

/**
 * A request parameter object for {@link Reply}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Data
public class ReplyRequest {

    @Schema(requiredMode = REQUIRED, minLength = 1)
    private String raw;

    @Schema(requiredMode = REQUIRED, minLength = 1)
    private String content;

    @Schema(defaultValue = "false")
    private Boolean allowNotification;

    @Schema(defaultValue = "false")
    private Boolean hidden;

    private CommentEmailOwner owner;

    private String quoteReply;

    /**
     * Converts {@link ReplyRequest} to {@link Reply}.
     *
     * @return a reply
     */
    public Reply toReply() {
        Reply reply = new Reply();
        reply.setMetadata(new Metadata());
        reply.getMetadata().setName(UUID.randomUUID().toString());

        Reply.ReplySpec spec = new Reply.ReplySpec();
        reply.setSpec(spec);
        spec.setRaw(raw);
        spec.setContent(content);
        spec.setAllowNotification(allowNotification);
        spec.setHidden(hidden);
        spec.setQuoteReply(quoteReply);

        if (owner != null) {
            spec.setOwner(owner.toCommentOwner());
        }
        return reply;
    }
}
