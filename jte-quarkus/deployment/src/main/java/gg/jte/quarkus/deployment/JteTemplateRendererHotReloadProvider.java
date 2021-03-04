package gg.jte.quarkus.deployment;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.quarkus.runtime.TemplateRenderer;
import gg.jte.resolve.DirectoryCodeResolver;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

@Singleton
public class JteTemplateRendererHotReloadProvider {

    @Produces
    public TemplateRenderer templateRenderer(JteConfiguration configuration) {
        String classPath = System.getProperty(TemplateRenderer.CLASS_PATH_HACK);
        String sourceDir = System.getProperty(TemplateRenderer.SOURCE_DIR_HACK);

        if (classPath == null) {
            throw new IllegalStateException("QUARKUS_CLASS_PATH_HACK not found, template engine cannot be created :-(");
        }

        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Paths.get(sourceDir));

        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, Paths.get("target", "jte-classes"), ContentType.valueOf(configuration.contentType), getClass().getClassLoader());
        templateEngine.setClassPath(Arrays.asList(classPath.split(File.pathSeparator)));

        return new TemplateRenderer(templateEngine);
    }
}
