package io.nebulacms.app.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtensionStoreUtilTest {

    Scheme scheme;

    Scheme grouplessScheme;

    @BeforeEach
    void setUp() {
        scheme = new Scheme(FakeExtension.class,
            new GroupVersionKind("fake.nebulacms.io", "v1alpha1", "Fake"),
            "fakes",
            "fake",
            new ObjectNode(null));
        grouplessScheme = new Scheme(FakeExtension.class,
            new GroupVersionKind("", "v1alpha1", "Fake"),
            "fakes",
            "fake",
            new ObjectNode(null));
    }

    @Test
    void buildStoreNamePrefix() {
        var prefix = ExtensionStoreUtil.buildStoreNamePrefix(scheme);
        assertEquals("/registry/fake.nebulacms.io/fakes", prefix);

        prefix = ExtensionStoreUtil.buildStoreNamePrefix(grouplessScheme);
        assertEquals("/registry/fakes", prefix);
    }

    @Test
    void buildStoreName() {
        var storeName = ExtensionStoreUtil.buildStoreName(scheme, "fake-name");
        assertEquals("/registry/fake.nebulacms.io/fakes/fake-name", storeName);

        storeName = ExtensionStoreUtil.buildStoreName(grouplessScheme, "fake-name");
        assertEquals("/registry/fakes/fake-name", storeName);
    }

}