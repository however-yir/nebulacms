package io.nebulacms.app.extension.exception;

import io.nebulacms.app.extension.GroupVersionKind;

import java.net.URI;
import org.springframework.http.HttpStatus;

public class ExtensionNotFoundException extends ExtensionException {

    public static final URI TYPE =
        URI.create("https://www.nebulacms.io/api/errors/extension-not-found");

    public ExtensionNotFoundException(GroupVersionKind gvk, String name) {
        super(HttpStatus.NOT_FOUND, "Extension " + gvk + "/" + name + " was not found.",
            null, null, new Object[] {gvk, name});
        setType(TYPE);
    }

}
