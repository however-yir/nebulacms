package io.nebulacms.app.theme.finders.vo;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import io.nebulacms.app.core.extension.User;
import io.nebulacms.app.core.user.service.UserService;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.extension.MetadataOperator;

/**
 * A value object for {@link io.nebulacms.app.core.extension.User}.
 *
 * @author guqing
 * @since 2.0.0
 */
@Value
@ToString
@Builder
public class ContributorVo implements ExtensionVoOperator {

    String name;

    String displayName;

    String avatar;

    String bio;

    String permalink;

    MetadataOperator metadata;

    /**
     * Convert {@link User} to {@link ContributorVo}.
     *
     * @param user user extension
     * @return contributor value object
     */
    public static ContributorVo from(User user) {
        User.UserStatus status = user.getStatus();
        String permalink = (status == null ? "" : status.getPermalink());
        return builder().name(user.getMetadata().getName())
            .displayName(user.getSpec().getDisplayName())
            .avatar(user.getSpec().getAvatar())
            .bio(user.getSpec().getBio())
            .permalink(permalink)
            .metadata(user.getMetadata())
            .build();
    }

    /**
     * Create a ghost contributor.
     *
     * @return a ghost contributor value object
     */
    public static ContributorVo ghost() {
        var metadata = new Metadata();
        metadata.setName(UserService.GHOST_USER_NAME);
        return builder()
            .name("ghost")
            .displayName("Ghost")
            // .avatar("/images/ghost.png")
            .bio("A ghost user.")
            .permalink("/authors/ghost")
            .metadata(metadata)
            .build();
    }
}
