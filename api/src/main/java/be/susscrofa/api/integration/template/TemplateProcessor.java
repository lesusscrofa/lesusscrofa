package be.susscrofa.api.integration.template;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public final class TemplateProcessor {

    private static final String ROOT_PARAMETER = "data";

    private final TemplateEngine templateEngine;

    public TemplateProcessor(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String process(final String templateName, final Object data) {
        final var context = new Context();
        context.setVariable(ROOT_PARAMETER, data);
        return templateEngine.process(templateName, context);
    }
}
