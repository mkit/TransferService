package org.mkit.microservice.transfer;

import com.google.common.base.Strings;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mkit.microservice.transfer.model.Transfer;

import static org.mkit.microservice.transfer.Constants.*;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class TransferVerticleTest extends BaseUnitTest {

  private Vertx vertx;

  @Before
  public void setUp(final TestContext tc) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(TransferVerticle.class.getName(), tc.asyncAssertSuccess());
  }

  @After
  public void tearDown(final TestContext tc) {
    vertx.close(tc.asyncAssertSuccess());
  }

  @Test
  public void testThatGetAllReturnsEmptyList(final TestContext tc) {
    // when
    final Async async = tc.async();
    vertx.createHttpClient().getNow(DEFAULT_PORT, DEFAULT_HOST, PATH_GET, response -> {
      tc.assertEquals(response.statusCode(), 200);
      response.bodyHandler(body -> {
        tc.assertTrue(body.toJsonArray().isEmpty());
        async.complete();
      });
    });
  }

  @Test
  public void testThatPostReturnsTransferWithId(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", "to", 10.0, "123");
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 201);
      response.bodyHandler(body -> {
        final Transfer created = new Transfer(body.toJsonObject());
        tc.assertFalse(Strings.isNullOrEmpty(created.getId()));
        tc.assertEquals(toCreate.getIssuer(), created.getIssuer());
        tc.assertEquals(toCreate.getFromAccount(), created.getFromAccount());
        tc.assertEquals(toCreate.getToAccount(), created.getToAccount());
        tc.assertEquals(toCreate.getAmount(), created.getAmount());
        tc.assertEquals(toCreate.getTransaction(), created.getTransaction());
        tc.assertEquals(Transfer.Status.PENDING, created.getStatus());
        async.complete();
      });
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatGetReturnsTransfer(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", "to", 10.0, "123");
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, responsePost -> {
      responsePost.bodyHandler(bodyPost -> {
        final Transfer created = new Transfer(bodyPost.toJsonObject());
        final String pathWithId = String.format("%s/%s", PATH_GET, created.getId());
        vertx.createHttpClient().getNow(DEFAULT_PORT, DEFAULT_HOST, pathWithId, responseGet -> {
          tc.assertEquals(responseGet.statusCode(), 200);
          responseGet.bodyHandler(bodyGet -> {
            final Transfer retrieved = new Transfer(bodyGet.toJsonObject());
            tc.assertEquals(created.getId(), retrieved.getId());
            async.complete();
          });
        });
      });
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithTransferStatusSet(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", "to", 10.0, "123");
    toCreate.setStatus(Transfer.Status.SUCCESS);
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithTransferIdSet(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", "to", 10.0, "123");
    toCreate.setId("someId");
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithTransferCreatedAtSet(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", "to", 10.0, "123");
    toCreate.setCreatedAt(1L);
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithEmptyFomAccount(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", null, "to", 10.0, "123");
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithEmptyToAccount(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", null, 10.0, "123");
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithEmptyTransaction(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", null, 10.0, null);
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

  @Test
  public void testThatPostFailsWithInvalidAmount(final TestContext tc) {
    // given
    final Transfer toCreate = givenTransfer("issuer", "from", null, -10.0, null);
    vertx.eventBus().consumer(DEFAULT_ACCOUNTS_ADDRESS, event -> {
      event.reply(null);
    });

    // when
    final Async async = tc.async();
    vertx.createHttpClient().post(DEFAULT_PORT, DEFAULT_HOST, PATH_CREATE, response -> {
      tc.assertEquals(response.statusCode(), 400);
      async.complete();
    }).end(toCreate.toJson().encode());
  }

}
