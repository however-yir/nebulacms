package io.nebulacms.app.core.counter;

import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.Counter;
import io.nebulacms.app.extension.ExtensionUtil;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.index.query.Queries;

/**
 * Counter service implementation.
 *
 * @author guqing
 * @since 2.0.0
 */
@Service
public class CounterServiceImpl implements CounterService {

    private final ReactiveExtensionClient client;

    public CounterServiceImpl(ReactiveExtensionClient client) {
        this.client = client;
    }

    @Override
    public Mono<Counter> getByName(String counterName) {
        return client.fetch(Counter.class, counterName);
    }

    @Override
    public Flux<Counter> getByNames(Collection<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return Flux.empty();
        }
        var options = ListOptions.builder()
            .andQuery(Queries.in("metadata.name", names))
            .build();
        return client.listAll(Counter.class, options, ExtensionUtil.defaultSort());
    }

    @Override
    public Mono<Counter> deleteByName(String counterName) {
        return client.fetch(Counter.class, counterName)
            .flatMap(client::delete);
    }
}
