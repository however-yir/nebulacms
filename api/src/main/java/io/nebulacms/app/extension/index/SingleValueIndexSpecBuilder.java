package io.nebulacms.app.extension.index;

import io.nebulacms.app.extension.Extension;

import java.util.function.Function;

/**
 * Builder for {@link SingleValueIndexSpec}.
 *
 * @param <E> the type of extension
 * @param <K> the type of index key
 * @author johnniang
 * @since 2.22.0
 */
public interface SingleValueIndexSpecBuilder<E extends Extension, K extends Comparable<K>>
    extends IndexSpecBuilder<E, K, SingleValueIndexSpecBuilder<E, K>> {

    /**
     * Sets the index function.
     *
     * @param indexFunc the index function
     * @return the builder itself
     */
    SingleValueIndexSpecBuilder<E, K> indexFunc(Function<E, K> indexFunc);

}
