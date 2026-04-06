package io.nebulacms.app.theme.engine;

import io.nebulacms.app.theme.ThemeContext;

public interface ThemeTemplateAvailabilityProvider {

    boolean isTemplateAvailable(ThemeContext themeContext, String viewName);

}
