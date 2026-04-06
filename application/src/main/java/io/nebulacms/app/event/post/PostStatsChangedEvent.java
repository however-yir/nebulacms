package io.nebulacms.app.event.post;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import io.nebulacms.app.core.counter.MeterUtils;
import io.nebulacms.app.core.extension.Counter;
import io.nebulacms.app.core.extension.content.Post;

@Getter
public class PostStatsChangedEvent extends ApplicationEvent {
    private final Counter counter;

    public PostStatsChangedEvent(Object source, Counter counter) {
        super(source);
        this.counter = counter;
    }

    public String getPostName() {
        var counterName = counter.getMetadata().getName();
        return StringUtils.removeStart(counterName, MeterUtils.nameOf(Post.class, ""));
    }
}
