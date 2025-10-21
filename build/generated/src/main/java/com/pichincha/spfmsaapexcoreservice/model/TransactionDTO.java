package com.pichincha.spfmsaapexcoreservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pichincha.spfmsaapexcoreservice.model.AccountDTO;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TransactionDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-20T02:43:05.386267500-05:00[America/Bogota]", comments = "Generator version: 7.15.0")
public class TransactionDTO {

  private @Nullable Long transactionId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime date;

  /**
   * Gets or Sets transactionType
   */
  public enum TransactionTypeEnum {
    WITHDRAWAL("WITHDRAWAL"),
    
    DEPOSIT("DEPOSIT");

    private final String value;

    TransactionTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransactionTypeEnum fromValue(String value) {
      for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TransactionTypeEnum transactionType;

  private @Nullable Double amount;

  private @Nullable Double balance;

  private @Nullable Long accountId;

  private @Nullable AccountDTO account;

  public TransactionDTO transactionId(@Nullable Long transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
   */
  
  @Schema(name = "transactionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transactionId")
  public @Nullable Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(@Nullable Long transactionId) {
    this.transactionId = transactionId;
  }

  public TransactionDTO date(@Nullable OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public @Nullable OffsetDateTime getDate() {
    return date;
  }

  public void setDate(@Nullable OffsetDateTime date) {
    this.date = date;
  }

  public TransactionDTO transactionType(@Nullable TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  /**
   * Get transactionType
   * @return transactionType
   */
  
  @Schema(name = "transactionType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transactionType")
  public @Nullable TransactionTypeEnum getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(@Nullable TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
  }

  public TransactionDTO amount(@Nullable Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Double getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Double amount) {
    this.amount = amount;
  }

  public TransactionDTO balance(@Nullable Double balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   */
  
  @Schema(name = "balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("balance")
  public @Nullable Double getBalance() {
    return balance;
  }

  public void setBalance(@Nullable Double balance) {
    this.balance = balance;
  }

  public TransactionDTO accountId(@Nullable Long accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountId")
  public @Nullable Long getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable Long accountId) {
    this.accountId = accountId;
  }

  public TransactionDTO account(@Nullable AccountDTO account) {
    this.account = account;
    return this;
  }

  /**
   * Get account
   * @return account
   */
  @Valid 
  @Schema(name = "account", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account")
  public @Nullable AccountDTO getAccount() {
    return account;
  }

  public void setAccount(@Nullable AccountDTO account) {
    this.account = account;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionDTO transactionDTO = (TransactionDTO) o;
    return Objects.equals(this.transactionId, transactionDTO.transactionId) &&
        Objects.equals(this.date, transactionDTO.date) &&
        Objects.equals(this.transactionType, transactionDTO.transactionType) &&
        Objects.equals(this.amount, transactionDTO.amount) &&
        Objects.equals(this.balance, transactionDTO.balance) &&
        Objects.equals(this.accountId, transactionDTO.accountId) &&
        Objects.equals(this.account, transactionDTO.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, date, transactionType, amount, balance, accountId, account);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionDTO {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

