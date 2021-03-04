package gg.jte.quarkus.deployment;

import gg.jte.ContentType;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.io.File;

@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class JteConfiguration {
    /**
     * The directory where template files are located
     */
    @ConfigItem(defaultValue = "src/main/jte")
    File sourceDirectory;

    /**
     * The {@link ContentType} of jte templates
     */
    @ConfigItem(defaultValue = "Html")
    String contentType;
}
