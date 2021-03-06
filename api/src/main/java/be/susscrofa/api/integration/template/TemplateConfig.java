package be.susscrofa.api.integration.template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateConfig {
    @Bean
    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        var classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setPrefix("template/");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setSuffix(".xhtml");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        classLoaderTemplateResolver.setOrder(1);
        return classLoaderTemplateResolver;
    }
}
