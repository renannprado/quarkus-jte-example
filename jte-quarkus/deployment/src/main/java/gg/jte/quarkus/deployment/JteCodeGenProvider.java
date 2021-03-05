package gg.jte.quarkus.deployment;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.quarkus.runtime.JteTemplateRenderer;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.runtime.Constants;
import io.quarkus.bootstrap.model.AppDependency;
import io.quarkus.deployment.CodeGenContext;
import io.quarkus.deployment.CodeGenProvider;
import io.quarkus.runtime.LaunchMode;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

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
        determineHotReloadProperties(context);
        return false;
    }

    private void determineHotReloadProperties(CodeGenContext context) {
        if (System.getProperty(JteTemplateRenderer.JTE_QUARKUS_CLASS_PATH) != null && System.getProperty(JteTemplateRenderer.JTE_QUARKUS_SOURCE_DIR) != null) {
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

        System.setProperty(JteTemplateRenderer.JTE_QUARKUS_CLASS_PATH, classPath.toString());
        System.setProperty(JteTemplateRenderer.JTE_QUARKUS_SOURCE_DIR, context.inputDir().toAbsolutePath().toString());
    }
}
