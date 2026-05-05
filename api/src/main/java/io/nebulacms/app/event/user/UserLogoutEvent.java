package io.nebulacms.app.event.user;

import io.nebulacms.app.core.extension.User;
import io.nebulacms.app.plugin.SharedEvent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * User logout event.
 *
 * @author lywq
 **/
@SharedEvent
public class UserLogoutEvent extends ApplicationEvent {

    @Getter
    private final User user;

    public UserLogoutEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
