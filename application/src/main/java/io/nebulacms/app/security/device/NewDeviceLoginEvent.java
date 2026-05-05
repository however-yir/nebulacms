package io.nebulacms.app.security.device;

import io.nebulacms.app.core.extension.Device;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewDeviceLoginEvent extends ApplicationEvent {
    private final Device device;

    public NewDeviceLoginEvent(Object source, Device device) {
        super(source);
        this.device = device;
    }
}
