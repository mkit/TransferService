package org.mkit.microservice.transfer.service;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mkit.microservice.transfer.BaseUnitTest;
import org.mkit.microservice.transfer.Constants;
import org.mkit.microservice.transfer.dao.TransferRepository;
import org.mkit.microservice.transfer.message.AccountMessage;
import org.mkit.microservice.transfer.model.Transfer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Michal Kit on 14.05.2017.
 */
@RunWith(VertxUnitRunner.class)
public class TransferProcessorTest extends BaseUnitTest {

  private TransferProcessor processor;
  private EventBus eventBus;

  @Mock
  private TransferRepository repository;

  @Before
  public void setUp(final TestContext tc) {
    MockitoAnnotations.initMocks(this);
    this.eventBus = Vertx.vertx().eventBus();
    processor = new TransferProcessor(eventBus, repository, new JsonObject());
  }

  @Test
  public void testThatProcessTransferSendsDebitMessage(final TestContext tc) {
    final Transfer transfer = givenTransfer("issuer", "from", "to", 10.0, "123");
    final Async async = tc.async(2);
    eventBus.consumer(Constants.DEFAULT_ACCOUNTS_ADDRESS, event -> {
      final AccountMessage message = new AccountMessage((JsonObject) event.body());
      if (AccountMessage.Type.DEBIT.equals(message.getType())) {
        tc.assertEquals(transfer.getFromAccount(), message.getAccount());
        if (async.count() == 2) {
          // Debit message is first
          async.complete();
        }
      } else {
        async.countDown();
        event.reply(null);
      }
    });
    // when
    processor.processTransfer(transfer);
  }

  @Test
  public void testThatProcessTransferSendsCreditMessage(final TestContext tc) {
    final Transfer transfer = givenTransfer("issuer", "from", "to", 10.0, "123");

    // After that Credit message
    final Async async = tc.async(2);
    eventBus.consumer(Constants.DEFAULT_ACCOUNTS_ADDRESS, event -> {
      final AccountMessage message = new AccountMessage((JsonObject) event.body());
      if (AccountMessage.Type.CREDIT.equals(message.getType())) {
        tc.assertEquals(transfer.getToAccount(), message.getAccount());
        if (async.count() == 1) {
          async.complete();
        }
      } else {
        // Debit message goes first
        async.countDown();
        event.reply(null);
      }
    });
    // when
    processor.processTransfer(transfer);
  }

}
