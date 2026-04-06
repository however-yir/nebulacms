package io.nebulacms.app.infra.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import io.nebulacms.app.core.extension.content.Post;
import io.nebulacms.app.extension.ListResult;

class GenericClassUtilsTest {

    @Test
    void generateConcreteClass() {
        var clazz = GenericClassUtils.generateConcreteClass(ListResult.class, Post.class,
            () -> Post.class.getName() + "List");
        assertEquals("io.nebulacms.app.core.extension.content.PostList", clazz.getName());
        assertEquals("io.nebulacms.app.core.extension.content", clazz.getPackageName());
    }

}