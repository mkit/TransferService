package org.mkit.microservice.transfer;

import com.google.common.collect.Sets;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.mkit.microservice.transfer.dao.InMemoryTransferRepository;
import org.mkit.microservice.transfer.dao.TransferRepository;
import org.mkit.microservice.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.mkit.microservice.transfer.Constants.*;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class TransferVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(TransferVerticle.class);

  @Override
  public void start(Future<Void> future) throws Exception {
    log.info("Starting verticle ...");
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    initCors(router);

    final JsonObject config = vertx.getOrCreateContext().config();

    final TransferRepository repository = new InMemoryTransferRepository(vertx.sharedData());
    final TransferService service = new TransferService(vertx.eventBus(), repository, config);

    router.post(PATH_CREATE).handler(service::create);
    router.get(PATH_GET_ONE).handler(service::getOne);
    router.get(PATH_GET).handler(service::getAll);

    final String host = config.getString(HOST_KEY, DEFAULT_HOST);
    final int port = config.getInteger(PORT_KEY, DEFAULT_PORT);

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(port, host, result -> {
        if (result.succeeded()) {
          log.info("Http Server started");
          future.complete();
        } else {
          log.error("Server startup error", result.cause());
          future.fail(result.cause());
        }
      });
  }

  private void initCors(final Router router) {
    final Set<String> allowHeaders = Sets.newHashSet();
    allowHeaders.add(HEADER_KEY_X_REQUESTED_WITH);
    allowHeaders.add(HEADER_KEY_ACCESS_CONTROL_ALLOW_ORIGIN);
    allowHeaders.add(HEADER_KEY_ORIGIN);
    allowHeaders.add(HEADER_KEY_CONTENT_TYPE);
    allowHeaders.add(HEADER_KEY_ACCEPT);
    Set<HttpMethod> allowMethods = Sets.newHashSet();
    allowMethods.add(HttpMethod.GET);
    allowMethods.add(HttpMethod.POST);

    router.route().handler(CorsHandler.create("*")
      .allowedHeaders(allowHeaders)
      .allowedMethods(allowMethods));
  }

}
