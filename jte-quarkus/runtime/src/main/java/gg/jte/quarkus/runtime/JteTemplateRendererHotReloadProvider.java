package gg.jte.quarkus.runtime;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

@Singleton
public class JteTemplateRendererHotReloadProvider {

    @Produces
    @ApplicationScoped
    public TemplateRenderer templateRenderer(JteConfiguration configuration) {
        String classPath = System.getProperty(JteTemplateRenderer.JTE_QUARKUS_CLASS_PATH);
        String sourceDir = System.getProperty(JteTemplateRenderer.JTE_QUARKUS_SOURCE_DIR);

        if (classPath == null) {
            throw new IllegalStateException(JteTemplateRenderer.JTE_QUARKUS_CLASS_PATH + " not found, template engine cannot be created :-(");
        }

        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Paths.get(sourceDir));

        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, Paths.get("target", "jte-classes"), ContentType.valueOf(configuration.contentType), getClass().getClassLoader());
        templateEngine.setClassPath(Arrays.asList(classPath.split(File.pathSeparator)));

        return new JteTemplateRenderer(templateEngine);
    }
}
