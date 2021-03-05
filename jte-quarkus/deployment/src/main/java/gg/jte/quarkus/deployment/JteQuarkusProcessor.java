package gg.jte.quarkus.deployment;

import gg.jte.TemplateEngine;
import gg.jte.quarkus.runtime.JteConfiguration;
import gg.jte.quarkus.runtime.JteTemplateEngineFactory;
import gg.jte.quarkus.runtime.JteTemplateRendererHotReloadProvider;
import gg.jte.quarkus.runtime.JteTemplateRendererPrecompiledProvider;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.GeneratedClassBuildItem;
import io.quarkus.runtime.LaunchMode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class JteQuarkusProcessor {

    private static final String FEATURE = "jte-quarkus";

    JteConfiguration configuration;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = IsDevMode.class)
    public void enableHotReloadTemplates(BuildProducer<AdditionalBeanBuildItem> beans) {
        beans.produce(AdditionalBeanBuildItem.builder().addBeanClass(JteTemplateRendererHotReloadProvider.class).setUnremovable().build());
    }

    @BuildStep(onlyIfNot = IsDevMode.class)
    public void enablePrecompiledTemplates(BuildProducer<AdditionalBeanBuildItem> beans) {
        beans.produce(new AdditionalBeanBuildItem(JteTemplateRendererPrecompiledProvider.class));
    }

    @BuildStep(onlyIfNot = IsDevMode.class)
    public void precompileTemplates(BuildProducer<GeneratedClassBuildItem> classes) {

        Path root = Paths.get("target", "jte-classes");

        TemplateEngine templateEngine = JteTemplateEngineFactory.create(configuration, null, root);
        templateEngine.precompileAll();

        try (Stream<Path> stream = Files.walk(root)) {
            stream
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".class"))
                    .map(p -> createGeneratedClassBuildItem(root, p))
                    .forEach(classes::produce);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to collect compiled templates in " + root, e);
        }
    }

    private GeneratedClassBuildItem createGeneratedClassBuildItem(Path root, Path classFile) {
        String qualifiedName = getQualifiedName(root, classFile);
        try {
            byte[] data = Files.readAllBytes(classFile);
            return new GeneratedClassBuildItem(true, qualifiedName, data);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read generated template class file " + classFile, e);
        }
    }

    private String getQualifiedName(Path root, Path classFile) {
        String name = root.relativize(classFile).toString();
        name = name.replace('\\', '/');
        name = name.replace('/', '.');
        name = name.substring(0, name.length() - ".class".length());
        return name;
    }

    static class IsDevMode implements BooleanSupplier {
        LaunchMode launchMode;

        public boolean getAsBoolean() {
            return launchMode == LaunchMode.DEVELOPMENT || launchMode == LaunchMode.TEST;
        }
    }
}
