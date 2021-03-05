package gg.jte.quarkus.runtime;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class JteTemplateRendererPrecompiledProvider {

    @Produces
    @ApplicationScoped
    public TemplateRenderer templateRenderer(JteConfiguration configuration) {
        TemplateEngine templateEngine = TemplateEngine.createPrecompiled(ContentType.valueOf(configuration.contentType));
        return new JteTemplateRenderer(templateEngine);
    }
}
