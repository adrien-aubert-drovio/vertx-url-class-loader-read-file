package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleReadDir extends AbstractVerticle {
  public void start(Promise<Void> promise) {
    vertx.setTimer(500L, id -> {
      vertx.fileSystem().readDir("org/example/verticle")
          .onSuccess(res -> promise.complete())
          .onFailure(e -> promise.fail(e));
    });
  }
}