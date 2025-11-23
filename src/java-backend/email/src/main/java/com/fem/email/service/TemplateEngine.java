package com.fem.email.service;

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class responsible for loading and rendering email templates.
 * Templates are loaded from the classpath and cached in memory for faster reuse.
 *
 * It supports simple placeholder replacement using the syntax: {{key}}.
 * If a placeholder required by the template is missing, a {@link TemplateError} is thrown.
 */
public class TemplateEngine {

    /** In-memory cache storing template contents by filename */
    private static final Map<String, String> TEMPLATE_CACHE = new ConcurrentHashMap<>();

    /**
     * Renders a template by loading it (from cache or filesystem) and replacing
     * its placeholders with the provided parameters.
     *
     * @param templateName the filename of the template inside /templates/
     * @param params       key–value pairs to replace inside the template
     * @return the rendered template as a String
     * @throws TemplateError if a placeholder is missing or rendering fails
     */
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

    /**
     * Loads a template file from the classpath under /templates/ and returns its content as a String.
     * The result is cached by the caller.
     *
     * @param templateName name of the template file
     * @return the raw template content
     * @throws TemplateError if the file does not exist or cannot be read
     */
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