package gg.jte.quarkus.runtime;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;

import java.util.Map;

public class TemplateRenderer {

    public static final String CLASS_PATH_HACK = "JTE_QUARKUS_CLASS_PATH_HACK";
    public static final String SOURCE_DIR_HACK = "JTE_QUARKUS_SOURCE_DIR_HACK";

    private final TemplateEngine templateEngine;

    public TemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String name, Map<String, Object> params) {
        StringOutput output = new StringOutput();
        templateEngine.render(name, params, output);
        return output.toString();
    }

    // TODO add more methods...
}
