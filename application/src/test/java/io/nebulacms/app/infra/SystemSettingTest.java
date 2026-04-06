package io.nebulacms.app.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import io.nebulacms.app.extension.ConfigMap;
import io.nebulacms.app.infra.SystemSetting.Comment;
import io.nebulacms.app.infra.SystemSetting.ExtensionPointEnabled;
import io.nebulacms.app.infra.utils.JsonUtils;

class SystemSettingTest {

    @Nested
    class ExtensionPointEnabledTest {

        @Test
        void deserializeTest() {
            var json = """
                    {
                      "io.nebulacms.app.search.post.PostSearchService": [
                        "io.nebulacms.app.search.post.LucenePostSearchService"
                      ]
                    }
                """;

            var enabled = JsonUtils.jsonToObject(json, ExtensionPointEnabled.class);
            assertTrue(enabled.containsKey("io.nebulacms.app.search.post.PostSearchService"));
        }
    }

    @Test
    void shouldGetConfigFromJson() {
        var configMap = new ConfigMap();
        configMap.putDataItem("comment", """
            {"enable": true}
            """);
        var comment = SystemSetting.get(configMap.getData(), Comment.GROUP, Comment.class);
        assertTrue(comment.getEnable());
    }

    @Test
    void shouldGetNullIfKeyNotExist() {
        var configMap = new ConfigMap();
        configMap.setData(new HashMap<>());
        String fake = SystemSetting.get(configMap.getData(), "fake-key", String.class);
        assertNull(fake);
    }

    @Test
    void shouldGetConfigViaConversionService() {
        var configMap = new ConfigMap();
        configMap.putDataItem("int", "100");
        var integer = SystemSetting.get(configMap.getData(), "int", Integer.class);
        assertEquals(100, integer);
    }
}