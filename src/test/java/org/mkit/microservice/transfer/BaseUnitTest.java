package org.mkit.microservice.transfer;

import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.runner.RunWith;
import org.mkit.microservice.transfer.model.Transfer;

/**
 * Created by Michal Kit on 14.05.2017.
 */
@RunWith(VertxUnitRunner.class)
public abstract class BaseUnitTest {


  protected static Transfer givenTransfer(final String issuer,
                                          final String fromAccount,
                                          final String toAccount,
                                          final Double amount,
                                          final String transaction) {
    return Transfer.builder()
      .withIssuer(issuer)
      .withFromAccount(fromAccount)
      .withToAccount(toAccount)
      .withtAmount(amount)
      .withTransaction(transaction)
      .build();
  }
}
