package gg.jte.quarkus.deployment;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.quarkus.runtime.TemplateRenderer;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class JteTemplateRendererPrecompiledProvider {

    @Produces
    public TemplateRenderer templateRenderer(JteConfiguration configuration) {
        TemplateEngine templateEngine = TemplateEngine.createPrecompiled(ContentType.valueOf(configuration.contentType));
        return new TemplateRenderer(templateEngine);
    }
}
