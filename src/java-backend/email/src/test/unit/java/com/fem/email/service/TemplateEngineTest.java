package com.fem.email.service;

import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class TemplateEngineTest {

    @SuppressWarnings("unchecked")
    private void clearCache() throws Exception {
        Field cacheField = TemplateEngine.class.getDeclaredField("TEMPLATE_CACHE");
        cacheField.setAccessible(true);
        ((Map<String, String>) cacheField.get(null)).clear();
    }

    @BeforeEach
    void setup() throws Exception {
        clearCache();
    }

    @Test
    void render_UsesCacheOnSecondCall() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("eventName", "Tech Conference");
        params.put("maxCapacity", 50);

        long start1 = System.nanoTime();
        String result1 = TemplateEngine.render("capacity_reached.html", params);
        long duration1 = System.nanoTime() - start1;

        long start2 = System.nanoTime();
        String result2 = TemplateEngine.render("capacity_reached.html", params);
        long duration2 = System.nanoTime() - start2;

        Assertions.assertEquals(result1, result2,
                "The rendered output should be identical when using the cached template");

        Assertions.assertTrue(duration2 < duration1 * 0.2,
                "Second render should be significantly faster due to caching");
    }
}