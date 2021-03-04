package gg.jte.quarkus.test;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JteQuarkusDevModeTest {

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest test = new QuarkusDevModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClass(GreetingResource.class)
            );

    private static Field deploymentSourcePath;

    @BeforeAll
    static void beforeAll() throws Exception {
        deploymentSourcePath = test.getClass().getDeclaredField("deploymentSourcePath");
        deploymentSourcePath.setAccessible(true);
    }

    @Test
    public void writeYourOwnDevModeTest() {
        addOrModifyJteFile("hello.jte", "Hello World");

        RestAssured.when().get("/test").then()
                .statusCode(200)
                .body(CoreMatchers.is("Hello World"));

        addOrModifyJteFile("hello.jte", "Hello World!!!");

        RestAssured.when().get("/test").then()
                .statusCode(200)
                .body(CoreMatchers.is("Hello World!!!"));


        // Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information
        // Assertions.assertTrue(true, "Add dev mode assertions to " + getClass().getName());
    }

    public void addOrModifyJteFile(String file, String content) {
        try {
            Path path = copy(file, content);

            test.sleepForFileChanges(path);
            // since this is a new file addition, even wait for the parent dir's last modified timestamp to change
            test.sleepForFileChanges(path.getParent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path copy(String file, String content) throws Exception {
        Path jteSources = ((Path) deploymentSourcePath.get(test)).getParent().resolve("jte");

        try {
            Path resolved = jteSources.resolve(file);
            Files.createDirectories(resolved.getParent());
            Files.write(resolved, content.getBytes(StandardCharsets.UTF_8));
            return resolved;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
