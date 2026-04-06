package io.nebulacms.app.extension;

@FunctionalInterface
public interface ExtensionMatcher {

    boolean match(Extension extension);

}
