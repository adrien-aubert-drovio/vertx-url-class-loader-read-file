package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleReadFile extends AbstractVerticle {
  public void start(Promise<Void> promise) {
    vertx.setTimer(500L, id -> {
      vertx.fileSystem().readFile("org/example/verticle/sample.xml")
          .onSuccess(res -> promise.complete())
          .onFailure(e -> promise.fail(e));
    });
  }
}