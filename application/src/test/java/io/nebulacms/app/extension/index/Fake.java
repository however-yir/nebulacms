package io.nebulacms.app.extension.index;

import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@GVK(
    group = "fake.halo.app",
    version = "v1",
    kind = "Fake",
    singular = "fake",
    plural = "fakes"
)
@Data
@EqualsAndHashCode(callSuper = true)
class Fake extends AbstractExtension {

    private Set<String> stringValues = new HashSet<>();

    private String stringValue;

}
