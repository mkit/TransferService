package org.mkit.microservice.transfer.dao;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import org.mkit.microservice.transfer.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class InMemoryTransferRepository implements TransferRepository {

  private static final Logger log = LoggerFactory.getLogger(InMemoryTransferRepository.class);

  private static final String MAP_NAME = "transfer";

  private final LocalMap<String, String> transfersMap;

  public InMemoryTransferRepository(final SharedData sharedData) {
    this.transfersMap = sharedData.getLocalMap(MAP_NAME);
  }

  @Override
  public Future<Transfer> insert(final Transfer transfer) throws IllegalArgumentException {
    final Transfer result = serializeAndStore(transfer);
    if (result == null) {
      log.debug("Transfer created: {}", transfer.getId());
      return Future.succeededFuture(transfer);
    } else {
      // Transfer with this ID already exists
      log.warn("Transfer already exists: {}", transfer.getId());
      return Future.failedFuture(new IllegalArgumentException(format("Transfer %s already exists", transfer.getId())));
    }
  }

  @Override
  public Future<Transfer> findOne(final String id) {
    return Future.succeededFuture(getAndDeserialize(id));
  }

  @Override
  public Future<List<Transfer>> findAll() {
    return Future.succeededFuture(getAndDeserializeAll());
  }

  @Override
  public Future<Transfer> updateStatus(final String id, final Transfer.Status status, final String message) {
    final Transfer transfer = getAndDeserialize(id);
    if (transfer != null) {
      log.debug("Updating transfer status: ", id);
      transfer.setStatus(status);
      transfer.setMessage(message);
      serializeAndStore(transfer);
    }
    return Future.succeededFuture(transfer);
  }

  private Transfer serializeAndStore(final Transfer transfer) {
    final String result = transfersMap.put(transfer.getId(), transfer.toJson().encode());
    if (!Strings.isNullOrEmpty(result)) {
      return Json.decodeValue(result, Transfer.class);
    }
    return null;
  }

  private Transfer getAndDeserialize(final String id) {
    final String result = transfersMap.get(id);
    if (!Strings.isNullOrEmpty(result)) {
      return Json.decodeValue(result, Transfer.class);
    }
    return null;
  }

  private List<Transfer> getAndDeserializeAll() {
    final List<Transfer> result = Lists.newArrayList();
    for (final String transferString: transfersMap.values()) {
      result.add(Json.decodeValue(transferString, Transfer.class));
    }
    return result;
  }
}
