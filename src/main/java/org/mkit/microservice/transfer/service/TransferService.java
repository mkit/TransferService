package org.mkit.microservice.transfer.service;

import com.google.common.base.Strings;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.mkit.microservice.transfer.dao.TransferRepository;
import org.mkit.microservice.transfer.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.mkit.microservice.transfer.Constants.*;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class TransferService {

  private static final Logger log = LoggerFactory.getLogger(TransferService.class);

  private final TransferRepository repository;
  private final TransferProcessor processor;

  public TransferService(final EventBus eventBus, final TransferRepository repository, final JsonObject config) {
    this.repository = repository;
    this.processor = new TransferProcessor(eventBus, repository, config);
  }

  public void create(final RoutingContext context) {
    final Transfer transfer = new Transfer(context.getBodyAsJson());

    final String validationError = getValidationError(transfer);
    if (!Strings.isNullOrEmpty(validationError)) {
      badRequest(context.response(), validationError);
      return;
    }

    transfer.setId(UUID.randomUUID().toString());
    transfer.setCreatedAt(System.currentTimeMillis());
    transfer.setStatus(Transfer.Status.PENDING);
    transfer.setMessage("Transfer is being processed");
    final Future<Transfer> promise = repository.insert(transfer);
    promise.setHandler(asyncResult -> {
      if (asyncResult.succeeded()) {
        final Transfer result = asyncResult.result();
        log.debug("Successful transfer creation: {}", result);
        context.response()
          .putHeader(HEADER_KEY_CONTENT_TYPE, HEADER_APPLICATION_JSON_ENCODING)
          .setStatusCode(201)
          .end(result.toJson().encodePrettily());
        processor.processTransfer(transfer);
      } else {
        internalServerError(context.response());
      }
    });
  }

  public void getOne(final RoutingContext context) {
    final String transferId = context.pathParam(PARAM_TRANSFER_ID);
    if (Strings.isNullOrEmpty(transferId)) {
      badRequest(context.response(), "Missing parameter: transferId");
      return;
    }
    final Future<Transfer> promise = repository.findOne(transferId);
    promise.setHandler(asyncResult -> {
      if (asyncResult.succeeded()) {
        final Transfer result = asyncResult.result();
        if (result == null) {
          notFound(context.response());
        } else {
          context.response()
            .putHeader(HEADER_KEY_CONTENT_TYPE, HEADER_APPLICATION_JSON_ENCODING)
            .setStatusCode(200)
            .end(result.toJson().encodePrettily());
        }
      } else {
        internalServerError(context.response());
      }
    });
  }

  public void getAll(final RoutingContext context) {
    final Future<List<Transfer>> promise = repository.findAll();
    promise.setHandler(asyncResult -> {
      if (asyncResult.succeeded()) {
        final List<Transfer> result = asyncResult.result();
        context.response()
          .putHeader(HEADER_KEY_CONTENT_TYPE, HEADER_APPLICATION_JSON_ENCODING)
          .setStatusCode(200)
          .end(Json.encodePrettily(result));
      } else {
        internalServerError(context.response());
      }
    });
  }

  private void badRequest(final HttpServerResponse response, final String message) {
    log.debug("Bad request: {}", message);
    response.setStatusCode(400).end(message);
  }

  private void notFound(final HttpServerResponse response) {
    response.setStatusCode(404).end();
  }

  private void internalServerError(final HttpServerResponse response) {
    response.setStatusCode(500).end();
  }

  private static String getValidationError(final Transfer transfer) {
    StringBuilder builder = new StringBuilder();
    if (!Strings.isNullOrEmpty(transfer.getId())) {
      builder.append("id field is read-only\n");
    }
    if (transfer.getCreatedAt() != null) {
      builder.append("createdAt field is read-only\n");
    }
    if (transfer.getStatus() != null) {
      builder.append("status field is read-only\n");
    }
    if (Strings.isNullOrEmpty(transfer.getFromAccount())) {
      builder.append("fromAccount cannot be empty\n");
    }
    if (Strings.isNullOrEmpty(transfer.getToAccount())) {
      builder.append("toAccount cannot be empty\n");
    }
    if (Strings.isNullOrEmpty(transfer.getTransaction())) {
      builder.append("transaction cannot be empty\n");
    }
    if (transfer.getAmount() == null || transfer.getAmount() <= 0.0) {
      builder.append("amount must be >= 0.0\n");
    }
    return builder.toString();
  }
}
