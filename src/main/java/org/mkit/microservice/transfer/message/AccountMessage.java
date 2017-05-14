package org.mkit.microservice.transfer.message;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Created by Michal Kit on 14.05.2017.
 */
@DataObject(generateConverter = true)
public class AccountMessage {

  private String account;
  private Double amount;
  private String transaction;
  private String transfer;
  private Type type;

  public AccountMessage() {
    // Do nothing
  }

  public AccountMessage(final JsonObject json) {
    AccountMessageConverter.fromJson(json, this);
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(final String account) {
    this.account = account;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(final Double amount) {
    this.amount = amount;
  }

  public String getTransaction() {
    return transaction;
  }

  public void setTransaction(final String transaction) {
    this.transaction = transaction;
  }

  public String getTransfer() {
    return transfer;
  }

  public void setTransfer(final String transfer) {
    this.transfer = transfer;
  }

  public Type getType() {
    return type;
  }

  public void setType(final Type type) {
    this.type = type;
  }

  public static enum Type {
    DEBIT, CREDIT
  }

  public JsonObject toJson() {
    final JsonObject result = new JsonObject();
    AccountMessageConverter.toJson(this, result);
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof AccountMessage)) return false;

    final AccountMessage that = (AccountMessage) o;

    if (account != null ? !account.equals(that.account) : that.account != null) return false;
    if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
    if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;
    if (transfer != null ? !transfer.equals(that.transfer) : that.transfer != null) return false;
    return type == that.type;
  }

  @Override
  public int hashCode() {
    int result = account != null ? account.hashCode() : 0;
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
    result = 31 * result + (transfer != null ? transfer.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AccountMessage{" +
      "account='" + account + '\'' +
      ", amount=" + amount +
      ", transaction='" + transaction + '\'' +
      ", transfer='" + transfer + '\'' +
      ", type=" + type +
      '}';
  }
}
