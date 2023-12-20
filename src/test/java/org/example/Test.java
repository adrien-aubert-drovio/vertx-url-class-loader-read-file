package org.example;

import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class Test {

  public static final String VERTICLE_INPUT_STREAM = "org.example.verticle.VerticleInputStream";
  private final static String JAR_FILE_PATH = "myjar.jar";
  public static final String VERTICLE_READ_DIR = "org.example.verticle.VerticleReadDir";
  public static final String VERTICLE_READ_FILE = "org.example.verticle.VerticleReadFile";

  private static int execute(String... command) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder()
        .command(command)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT);
    Process process = builder.start();
    return process.waitFor();
  }

  /**
   * Creates the JAR file that will be loaded.
   * Be sure to compile the test classes before running the tests.
   * @throws IOException
   * @throws InterruptedException
   */
  @BeforeAll
  public static void createJarFile() throws IOException, InterruptedException {
    int code = execute("cp", "src/test/resources/sample/sample.xml", "target/test-classes/org/example/verticle");
    assertEquals(0, code, "Fail to copy sample resource.");
    code = execute("jar", "cf", JAR_FILE_PATH, "-C", "target/test-classes", "org/example/verticle");
    assertEquals(0, code, "Fail to generate test plugin. Check that the tests sources have been compiled.");
    code = execute("rm", "-rf", "target/test-classes/org/example/verticle");
    assertEquals(0, code, "Fail to remove test classes");
  }

  @org.junit.jupiter.api.Test
  public void readDir(VertxTestContext should) throws IOException {
    Vertx vertx = Vertx.vertx();
    URL[] urls = {new File(JAR_FILE_PATH).toURI().toURL()};

    ClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    DeploymentOptions opts = new DeploymentOptions().setClassLoader(cl);
    CompositeFuture.all(vertx.deployVerticle(VERTICLE_INPUT_STREAM, opts), vertx.deployVerticle(VERTICLE_READ_DIR, opts))
        .onComplete(should.succeedingThenComplete());
  }


  @org.junit.jupiter.api.Test
  public void readFile(VertxTestContext should) throws IOException {
    Vertx vertx = Vertx.vertx();
    URL[] urls = {new File(JAR_FILE_PATH).toURI().toURL()};

    ClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    DeploymentOptions opts = new DeploymentOptions().setClassLoader(cl);
    CompositeFuture.all(vertx.deployVerticle(VERTICLE_INPUT_STREAM, opts), vertx.deployVerticle(VERTICLE_READ_FILE, opts))
        .onComplete(should.succeedingThenComplete());
  }
}