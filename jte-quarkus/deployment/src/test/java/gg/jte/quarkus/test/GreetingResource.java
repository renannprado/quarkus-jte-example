package gg.jte.quarkus.test;

import gg.jte.quarkus.runtime.JteConfiguration;
import gg.jte.quarkus.runtime.TemplateRenderer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Path("/test")
public class GreetingResource {

    //@Inject
    //TemplateRenderer templateRenderer;
    @Inject
    JteConfiguration configuration;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String render() {
        //return templateRenderer.render("hello.jte", Collections.emptyMap());
        return "foo";
    }
}