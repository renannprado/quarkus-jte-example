package gg.jte.quarkus.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.runtime.LaunchMode;

import javax.inject.Inject;
import java.util.function.BooleanSupplier;

public class JteQuarkusProcessor {

    private static final String FEATURE = "jte-quarkus";

    @Inject
    JteConfiguration configuration;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = IsDevMode.class)
    public void enableHotReloadTemplates(BuildProducer<AdditionalBeanBuildItem> beans) {
        beans.produce(new AdditionalBeanBuildItem(JteTemplateRendererHotReloadProvider.class));
    }

    @BuildStep(onlyIfNot = IsDevMode.class)
    public void enablePrecompiledTemplates(BuildProducer<AdditionalBeanBuildItem> beans) {
        beans.produce(new AdditionalBeanBuildItem(JteTemplateRendererPrecompiledProvider.class));
    }

    static class IsDevMode implements BooleanSupplier {
        LaunchMode launchMode;

        public boolean getAsBoolean() {
            return launchMode == LaunchMode.DEVELOPMENT;
        }
    }
}
