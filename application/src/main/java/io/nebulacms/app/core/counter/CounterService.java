package io.nebulacms.app.core.counter;

import io.nebulacms.app.core.extension.Counter;

import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author guqing
 * @since 2.0.0
 */
public interface CounterService {

    Mono<Counter> getByName(String counterName);

    Flux<Counter> getByNames(Collection<String> names);

    Mono<Counter> deleteByName(String counterName);
}
