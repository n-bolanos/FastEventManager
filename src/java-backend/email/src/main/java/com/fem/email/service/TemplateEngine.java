package com.fem.email.service;

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateEngine {

    public static String render(String templateName, Map<String, Object> params) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + templateName);
            byte[] bytes = resource.getInputStream().readAllBytes();
            String template = new String(bytes, StandardCharsets.UTF_8);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                template = template.replace(placeholder, entry.getValue().toString());
            }

            return template;
        } catch (Exception e) {
            return "<p>Error loading template: " + templateName + "</p>";
        }
    }
}
