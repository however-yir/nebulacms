package io.nebulacms.app.core.extension;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static io.nebulacms.app.core.extension.User.GROUP;
import static io.nebulacms.app.core.extension.User.KIND;
import static io.nebulacms.app.core.extension.User.VERSION;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;

/**
 * The extension represents user details of Halo.
 *
 * @author johnniang
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GVK(group = GROUP,
    version = VERSION,
    kind = KIND,
    singular = "user",
    plural = "users")
public class User extends AbstractExtension {

    public static final String GROUP = "";
    public static final String VERSION = "v1alpha1";
    public static final String KIND = "User";

    public static final String USER_RELATED_ROLES_INDEX = "roles";

    public static final String ROLE_NAMES_ANNO = "rbac.authorization.nebulacms.io/role-names";

    public static final String EMAIL_TO_VERIFY = "nebulacms.io/email-to-verify";

    public static final String LAST_AVATAR_ATTACHMENT_NAME_ANNO =
        "nebulacms.io/last-avatar-attachment-name";

    public static final String AVATAR_ATTACHMENT_NAME_ANNO = "nebulacms.io/avatar-attachment-name";

    public static final String HIDDEN_USER_LABEL = "nebulacms.io/hidden-user";

    public static final String REQUEST_TO_UPDATE = "nebulacms.io/request-to-update";

    @Schema(requiredMode = REQUIRED)
    private UserSpec spec = new UserSpec();

    private UserStatus status = new UserStatus();

    @Data
    public static class UserSpec {

        @Schema(requiredMode = REQUIRED)
        private String displayName;

        private String avatar;

        @Schema(requiredMode = REQUIRED)
        private String email;

        private boolean emailVerified;

        private String phone;

        private String password;

        private String bio;

        private Instant registeredAt;

        private Boolean twoFactorAuthEnabled;

        private String totpEncryptedSecret;

        private Boolean disabled;

        private Integer loginHistoryLimit;

    }

    @Data
    public static class UserStatus {

        private String permalink;

    }

}
