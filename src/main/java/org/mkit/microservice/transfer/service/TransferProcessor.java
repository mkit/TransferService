package org.mkit.microservice.transfer.service;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.mkit.microservice.transfer.dao.TransferRepository;
import org.mkit.microservice.transfer.message.AccountMessage;
import org.mkit.microservice.transfer.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mkit.microservice.transfer.Constants.ACCOUNTS_ADDRESS_KEY;
import static org.mkit.microservice.transfer.Constants.DEFAULT_ACCOUNTS_ADDRESS;
import static org.mkit.microservice.transfer.message.AccountMessage.Type;

/**
 * Created by Michal Kit on 14.05.2017.
 */
public class TransferProcessor {

  private static final Logger log = LoggerFactory.getLogger(TransferProcessor.class);

  private final EventBus eventBus;
  private final TransferRepository repository;
  private final String accountsAddress;

  public TransferProcessor(final EventBus eventBus, final TransferRepository repository, final JsonObject config) {
    this.eventBus = eventBus;
    this.repository = repository;
    this.accountsAddress = config.getString(ACCOUNTS_ADDRESS_KEY, DEFAULT_ACCOUNTS_ADDRESS);
  }

  public void processTransfer(final Transfer transfer) {
    log.debug("Sending DEBIT message to AccountsService: {}", transfer);
    final Future<Transfer> accountMessageFutures = sendAccountMessage(transfer, Type.DEBIT).compose(successVoid -> {
      log.debug("Sending CREDIT message to AccountsService: {}", transfer);
      return sendAccountMessage(transfer, Type.CREDIT);
    }).compose(successVoid -> {
      log.debug("Transfer successful: {}", transfer.getId());
      return repository.updateStatus(transfer.getId(), Transfer.Status.SUCCESS, "Transfer succeeded");
    }).recover(failure -> {
      final String causeMessage = failure.getCause().getMessage();
      log.warn("Transfer failed: id={}, cause={}", transfer.getId(), causeMessage);
      return repository.updateStatus(transfer.getId(), Transfer.Status.FAILED, causeMessage);
    }).setHandler(voidAsyncResult -> {
      if (voidAsyncResult.succeeded()) {
        log.debug("Transfer status successfully updated: {}", transfer.getId());
      } else {
        log.error("Transfer status update error", voidAsyncResult.cause());
      }
    });
  }

  private Future<Transfer> sendAccountMessage(final Transfer transfer, final Type type) {
    final AccountMessage message = Type.CREDIT.equals(type) ? buildCreditMessage(transfer) : buildDebitMessage(transfer);
    final Future<Transfer> promise = Future.future();
    eventBus.send(accountsAddress, message.toJson(), asyncResult -> {
      if (asyncResult.succeeded()) {
        promise.complete();
      } else {
        log.warn("Failure accounts messaging: {}", message, asyncResult.cause());
        promise.fail(asyncResult.cause());
      }
    });
    return promise;
  }

  private AccountMessage buildDebitMessage(final Transfer transfer) {
    final AccountMessage result = new AccountMessage();
    result.setAccount(transfer.getFromAccount());
    result.setAmount(transfer.getAmount());
    result.setTransaction(transfer.getTransaction());
    result.setType(AccountMessage.Type.DEBIT);
    return result;
  }

  private AccountMessage buildCreditMessage(final Transfer transfer) {
    final AccountMessage result = new AccountMessage();
    result.setAccount(transfer.getToAccount());
    result.setAmount(transfer.getAmount());
    result.setTransaction(transfer.getTransaction());
    result.setType(AccountMessage.Type.CREDIT);
    return result;
  }

}
