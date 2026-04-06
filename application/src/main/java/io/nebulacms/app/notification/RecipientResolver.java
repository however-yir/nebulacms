package io.nebulacms.app.notification;

import reactor.core.publisher.Flux;
import io.nebulacms.app.core.extension.notification.Reason;

public interface RecipientResolver {

    Flux<Subscriber> resolve(Reason reason);
}
