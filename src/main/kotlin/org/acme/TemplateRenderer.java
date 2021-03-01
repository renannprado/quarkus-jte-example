package org.acme;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.quarkus.runtime.Startup;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Map;

@Startup
@Singleton
public class TemplateRenderer {

    private final TemplateEngine templateEngine;

    public TemplateRenderer() {
        CodeResolver codeResolver = new DirectoryCodeResolver(Path.of(System.getProperty("user.dir"), "classes", "META-INF", "resources")); // This is the directory where your .jte files are located.

        templateEngine = TemplateEngine.create(codeResolver, Path.of("target/jte-classes"), ContentType.Html, Thread.currentThread().getContextClassLoader());
    }

    public String render(String name, Map<Object, Object> params) {
        StringOutput output = new StringOutput();
        templateEngine.render(name, params, output);
        return output.toString();
    }
}
