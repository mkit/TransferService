package org.mkit.microservice.transfer.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Created by Michal Kit on 14.05.2017.
 */
@DataObject(generateConverter = true)
public class Transfer {

  private String id;
  private String transaction;
  private String fromAccount;
  private String toAccount;
  private String issuer;
  private Double amount;
  private Status status;
  private String message;
  private Long createdAt;

  public Transfer() {
    // Do nothing;
  }

  private Transfer(final Builder builder) {
    this.id = builder.id;
    this.transaction = builder.transaction;
    this.fromAccount = builder.fromAccount;
    this.toAccount = builder.toAccount;
    this.issuer = builder.issuer;
    this.amount = builder.amount;
    this.status = builder.status;
    this.message = builder.message;
    this.createdAt = builder.createdAt;
  }

  public Transfer(final JsonObject json) {
    TransferConverter.fromJson(json, this);
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getTransaction() {
    return transaction;
  }

  public void setTransaction(final String transaction) {
    this.transaction = transaction;
  }

  public String getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(final String fromAccount) {
    this.fromAccount = fromAccount;
  }

  public String getToAccount() {
    return toAccount;
  }

  public void setToAccount(final String toAccount) {
    this.toAccount = toAccount;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(final String issuer) {
    this.issuer = issuer;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(final Double amount) {
    this.amount = amount;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final Long createdAt) {
    this.createdAt = createdAt;
  }

  public JsonObject toJson() {
    final JsonObject result = new JsonObject();
    TransferConverter.toJson(this, result);
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Transfer)) return false;

    final Transfer transfer = (Transfer) o;

    if (id != null ? !id.equals(transfer.id) : transfer.id != null) return false;
    if (transaction != null ? !transaction.equals(transfer.transaction) : transfer.transaction != null)
      return false;
    if (fromAccount != null ? !fromAccount.equals(transfer.fromAccount) : transfer.fromAccount != null) return false;
    if (toAccount != null ? !toAccount.equals(transfer.toAccount) : transfer.toAccount != null) return false;
    if (issuer != null ? !issuer.equals(transfer.issuer) : transfer.issuer != null) return false;
    if (amount != null ? !amount.equals(transfer.amount) : transfer.amount != null) return false;
    if (status != null ? !status.equals(transfer.status) : transfer.status != null) return false;
    if (message != null ? !message.equals(transfer.message) : transfer.message != null)
      return false;
    return createdAt != null ? createdAt.equals(transfer.createdAt) : transfer.createdAt == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
    result = 31 * result + (fromAccount != null ? fromAccount.hashCode() : 0);
    result = 31 * result + (toAccount != null ? toAccount.hashCode() : 0);
    result = 31 * result + (issuer != null ? issuer.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (message != null ? message.hashCode() : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Transfer{" +
      "id='" + id + '\'' +
      ", transaction='" + transaction + '\'' +
      ", fromAccount='" + fromAccount + '\'' +
      ", toAccount='" + toAccount + '\'' +
      ", issuer='" + issuer + '\'' +
      ", amount=" + amount +
      ", status='" + status + '\'' +
      ", message='" + message + '\'' +
      ", createdAt=" + createdAt +
      '}';
  }

  public static enum Status {
    SUCCESS, FAILED, PENDING
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String id;
    private String transaction;
    private String fromAccount;
    private String toAccount;
    private String issuer;
    private Double amount;
    private Status status;
    private String message;
    private Long createdAt;

    public Builder withId(final String id) {
      this.id = id;
      return this;
    }

    public Builder withTransaction(final String transaction) {
      this.transaction = transaction;
      return this;
    }

    public Builder withFromAccount(final String fromAccount) {
      this.fromAccount = fromAccount;
      return this;
    }

    public Builder withToAccount(final String toAccount) {
      this.toAccount = toAccount;
      return this;
    }

    public Builder withIssuer(final String issuer) {
      this.issuer = issuer;
      return this;
    }

    public Builder withtAmount(final Double amount) {
      this.amount = amount;
      return this;
    }

    public Builder withStatus(final Status status) {
      this.status = status;
      return this;
    }

    public Builder withMessage(final String message) {
      this.message = message;
      return this;
    }

    public Builder withCreatedAt(final Long createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Transfer build() {
      return new Transfer(this);
    }
  }
}
