package com.fem.email.service;

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateEngine {

    // Caching
    private static final Map<String, String> TEMPLATE_CACHE = new ConcurrentHashMap<>();

    public static String render(String templateName, Map<String, Object> params) {
        try {
            String template = TEMPLATE_CACHE.computeIfAbsent(
                    templateName,
                    TemplateEngine::loadTemplate
            );

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";

                if (!template.contains(placeholder)) {
                    throw new TemplateError("Missing placeholder: " + placeholder);
                }

                template = template.replace(placeholder, entry.getValue().toString());
            }

            return template;

        } catch (TemplateError e) {
            throw e;

        } catch (Exception e) {
            throw new TemplateError("Failed to render template: " + templateName, e);
        }
    }

    private static String loadTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + templateName);

            if (!resource.exists()) {
                throw new TemplateError("Template not found: " + templateName);
            }

            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);

        } catch (TemplateError e) {
            throw e;

        } catch (Exception e) {
            throw new TemplateError("Failed to load template: " + templateName, e);
        }
    }
}