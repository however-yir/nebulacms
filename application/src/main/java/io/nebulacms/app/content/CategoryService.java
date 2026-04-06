package io.nebulacms.app.content;

import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.nebulacms.app.core.extension.content.Category;

public interface CategoryService {

    Flux<Category> listChildren(@NonNull String categoryName);

    Mono<Category> getParentByName(@NonNull String categoryName);

    Mono<Boolean> isCategoryHidden(@NonNull String categoryName);
}
