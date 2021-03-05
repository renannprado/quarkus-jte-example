package gg.jte.quarkus.runtime;

import gg.jte.ContentType;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class JteConfiguration {

    public static JteConfiguration INSTANCE;
    /**
     * The {@link ContentType} of jte templates
     */
    @ConfigItem(defaultValue = "Html")
    public String contentType;

    public JteConfiguration() {
        INSTANCE = this;
    }
}
