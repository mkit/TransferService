package org.mkit.microservice.transfer.dao;

import io.vertx.core.Future;
import org.mkit.microservice.transfer.model.Transfer;

import java.util.List;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public interface TransferRepository {

  Future<Transfer> insert(Transfer transfer) throws IllegalArgumentException;

  Future<Transfer> findOne(String id);

  Future<List<Transfer>> findAll();

  Future<Transfer> updateStatus(String id, Transfer.Status status, String message);

}
