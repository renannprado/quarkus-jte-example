package org.acme

import gg.jte.quarkus.runtime.JteConfiguration
import gg.jte.quarkus.runtime.TemplateRenderer
import io.fabric8.kubernetes.client.KubernetesClient
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.Path

@Path("/test")
class GreetingResource(
        private val kubernetesClient: KubernetesClient
) {
    @Inject
    lateinit var templateRenderer: TemplateRenderer

    @Inject
    lateinit var config: JteConfiguration

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun hello() : String {
        return templateRenderer.render("example1.jte", mapOf("foo" to "bar2212"))
    }
}