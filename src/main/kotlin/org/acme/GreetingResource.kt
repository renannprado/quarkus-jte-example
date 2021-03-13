package org.acme

import gg.jte.CodeResolver
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.TemplateOutput
import gg.jte.output.StringOutput
import gg.jte.resolve.DirectoryCodeResolver
import io.fabric8.kubernetes.client.KubernetesClient
import java.nio.file.Path
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.Path as HttpPath
import gg.jte.html.HtmlInterceptor
import gg.jte.html.HtmlTemplateOutput

@HttpPath("/test")
class GreetingResource(
        private val kubernetesClient: KubernetesClient
) {

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun hello() : String {
        var x : HtmlInterceptor? = null
        var y : HtmlTemplateOutput? = null
        val workingDir = System.getProperty("user.dir")
        val codeResolver: CodeResolver = DirectoryCodeResolver(Path.of("${workingDir}/classes/META-INF/resources")) // This is the directory where your .jte files are located.

//        val templateEngine: TemplateEngine = TemplateEngine.create(codeResolver, ContentType.Html)
        val templateEngine: TemplateEngine = TemplateEngine.create(
                codeResolver,
                Path.of("${workingDir}/jte-classes"),
                ContentType.Html,
                Thread.currentThread().contextClassLoader
        )

        val output: TemplateOutput = StringOutput()
        templateEngine.render("example1.jte", mapOf(), output)

        return output.toString()
    }
}