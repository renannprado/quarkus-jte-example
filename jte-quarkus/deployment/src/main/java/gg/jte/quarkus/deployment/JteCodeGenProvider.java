package gg.jte.quarkus.deployment;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.runtime.Constants;
import io.quarkus.bootstrap.model.AppDependency;
import io.quarkus.deployment.CodeGenContext;
import io.quarkus.deployment.CodeGenProvider;
import io.quarkus.runtime.LaunchMode;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static gg.jte.quarkus.runtime.TemplateRenderer.CLASS_PATH_HACK;
import static gg.jte.quarkus.runtime.TemplateRenderer.SOURCE_DIR_HACK;

public class JteCodeGenProvider implements CodeGenProvider {


    @Override
    public String providerId() {
        return "jte";
    }

    @Override
    public String inputExtension() {
        return "jte";
    }

    @Override
    public String inputDirectory() {
        return "jte";
    }

    @Override
    public boolean trigger(CodeGenContext context) {
        if (LaunchMode.current() == LaunchMode.DEVELOPMENT) {
            determineHotReloadProperties(context);
            return false;
        } else {
            // TODO how to access config for content type and other settings?

            CodeResolver codeResolver = new DirectoryCodeResolver(context.inputDir());
            TemplateEngine templateEngine = TemplateEngine.create(codeResolver, context.outDir(), ContentType.Html, null, Constants.PACKAGE_NAME_PRECOMPILED);

            List<String> sources = templateEngine.generateAll();

            return !sources.isEmpty();
        }
    }

    private void determineHotReloadProperties(CodeGenContext context) {
        if (System.getProperty(CLASS_PATH_HACK) != null && System.getProperty(SOURCE_DIR_HACK) != null) {
            return;
        }

        StringBuilder classPath = new StringBuilder(512);
        for (AppDependency userDependency : context.appModel().getUserDependencies()) {
            for (Path path : userDependency.getArtifact().getPaths()) {
                if (classPath.length() > 0) {
                    classPath.append(File.pathSeparatorChar);
                }
                classPath.append(path.toAbsolutePath().toString());
            }
        }

        System.setProperty(CLASS_PATH_HACK, classPath.toString());
        System.setProperty(SOURCE_DIR_HACK, context.inputDir().toAbsolutePath().toString());
    }
}
