package io.nebulacms.app.content.impl;

import static io.nebulacms.app.extension.index.query.Queries.equal;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.content.CategoryService;
import io.nebulacms.app.core.extension.content.Category;
import io.nebulacms.app.extension.ListOptions;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.extension.PageRequestImpl;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.router.selector.FieldSelector;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ReactiveExtensionClient client;

    @Override
    public Flux<Category> listChildren(@NonNull String categoryName) {
        return client.fetch(Category.class, categoryName)
            .expand(category -> {
                var children = category.getSpec().getChildren();
                if (children == null || children.isEmpty()) {
                    return Mono.empty();
                }
                return Flux.fromIterable(children)
                    .flatMap(name -> client.fetch(Category.class, name))
                    .filter(this::isNotIndependent);
            });
    }

    @Override
    public Mono<Category> getParentByName(@NonNull String name) {
        if (StringUtils.isBlank(name)) {
            return Mono.empty();
        }
        var listOptions = new ListOptions();
        listOptions.setFieldSelector(FieldSelector.of(
            equal("spec.children", name)
        ));
        return client.listBy(Category.class, listOptions,
                PageRequestImpl.of(1, 1, defaultSort())
            )
            .flatMap(result -> Mono.justOrEmpty(ListResult.first(result)));
    }

    @Override
    public Mono<Boolean> isCategoryHidden(@NonNull String categoryName) {
        return client.fetch(Category.class, categoryName)
            .expand(category -> getParentByName(category.getMetadata().getName()))
            .filter(category -> category.getSpec().isHideFromList())
            .hasElements();
    }

    static Sort defaultSort() {
        return Sort.by(Sort.Order.desc("spec.priority"),
            Sort.Order.desc("metadata.creationTimestamp"),
            Sort.Order.desc("metadata.name"));
    }

    private boolean isNotIndependent(Category category) {
        return !category.getSpec().isPreventParentPostCascadeQuery();
    }
}
