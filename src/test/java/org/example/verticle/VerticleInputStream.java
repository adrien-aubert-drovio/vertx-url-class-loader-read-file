package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.io.IOException;
import java.io.InputStream;

public class VerticleInputStream extends AbstractVerticle {
  private static final String FILE = "/org/example/verticle/sample.xml";

  public void start(Promise<Void> promise) {
    try {
      InputStream is = this.getClass().getResource(FILE).openStream();
      int count = is.available();
      if (count == 0) {
        is.close();
        promise.fail("InputStream first read failed");
        return;
      }
      vertx.setTimer(1000L, id -> {
        int count2 = 0;
        try {
          count2 = is.available();
          if (count2 == 0) {
            is.close();
            promise.fail("InputStream second read failed");
            return;
          }
          promise.complete();
        } catch (IOException e) {
          try {
            is.close();
          } catch (IOException ex) {
          }
          promise.fail(e);
        }
      });
    } catch (IOException e) {
      promise.fail(e);
    }
  }
}