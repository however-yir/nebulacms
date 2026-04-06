package io.nebulacms.app.core.extension.content;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;
import io.nebulacms.app.extension.GroupVersionKind;

/**
 * @author guqing
 * @see <a href="https://github.com/nebula-labs/nebulacms/issues/2322">issue#2322</a>
 * @since 2.0.0
 */
@Data
@ToString(callSuper = true)
@GVK(group = Constant.GROUP, version = Constant.VERSION,
    kind = Tag.KIND, plural = "tags", singular = "tag")
@EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractExtension {

    public static final String KIND = "Tag";

    public static final GroupVersionKind GVK = GroupVersionKind.fromExtension(Tag.class);

    public static final String REQUIRE_SYNC_ON_STARTUP_INDEX_NAME = "requireSyncOnStartup";

    @Schema(requiredMode = REQUIRED)
    private TagSpec spec;

    @Schema
    private TagStatus status;

    @Data
    public static class TagSpec {

        @Schema(requiredMode = REQUIRED, minLength = 1)
        private String displayName;

        @Schema(requiredMode = REQUIRED, minLength = 1)
        private String slug;

        private String description;

        /**
         * Color regex explanation.
         * <pre>
         * ^                 # start of the line
         * #                 # start with a number sign `#`
         * (                 # start of (group 1)
         *   [a-fA-F0-9]{6}  # support z-f, A-F and 0-9, with a length of 6
         *   |               # or
         *   [a-fA-F0-9]{3}  # support z-f, A-F and 0-9, with a length of 3
         * )                 # end of (group 1)
         * $                 # end of the line
         * </pre>
         */
        @Schema(pattern = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")
        private String color;

        private String cover;
    }

    @JsonIgnore
    public TagStatus getStatusOrDefault() {
        if (this.status == null) {
            this.status = new TagStatus();
        }
        return this.status;
    }

    @Data
    public static class TagStatus {

        private String permalink;

        public Integer visiblePostCount;

        public Integer postCount;

        private Long observedVersion;
    }
}
