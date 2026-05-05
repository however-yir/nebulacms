package io.nebulacms.app.notification;

import io.nebulacms.app.core.extension.notification.Reason;

import reactor.core.publisher.Flux;

public interface RecipientResolver {

    Flux<Subscriber> resolve(Reason reason);
}
