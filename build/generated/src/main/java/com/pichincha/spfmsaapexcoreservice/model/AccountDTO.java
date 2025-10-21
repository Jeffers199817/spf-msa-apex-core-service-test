package com.pichincha.spfmsaapexcoreservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pichincha.spfmsaapexcoreservice.model.ClientDTO;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AccountDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-20T02:43:05.386267500-05:00[America/Bogota]", comments = "Generator version: 7.15.0")
public class AccountDTO {

  private @Nullable Long accountId;

  private @Nullable String accountNumber;

  /**
   * Gets or Sets accountType
   */
  public enum AccountTypeEnum {
    SAVINGS("SAVINGS"),
    
    CHECKING("CHECKING");

    private final String value;

    AccountTypeEnum(String value) {
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
    public static AccountTypeEnum fromValue(String value) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AccountTypeEnum accountType;

  private @Nullable Double initialBalance;

  private @Nullable Boolean status;

  private @Nullable Long clientId;

  private @Nullable ClientDTO client;

  public AccountDTO accountId(@Nullable Long accountId) {
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

  public AccountDTO accountNumber(@Nullable String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * Get accountNumber
   * @return accountNumber
   */
  
  @Schema(name = "accountNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountNumber")
  public @Nullable String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(@Nullable String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public AccountDTO accountType(@Nullable AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   */
  
  @Schema(name = "accountType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountType")
  public @Nullable AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(@Nullable AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public AccountDTO initialBalance(@Nullable Double initialBalance) {
    this.initialBalance = initialBalance;
    return this;
  }

  /**
   * Get initialBalance
   * @return initialBalance
   */
  
  @Schema(name = "initialBalance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initialBalance")
  public @Nullable Double getInitialBalance() {
    return initialBalance;
  }

  public void setInitialBalance(@Nullable Double initialBalance) {
    this.initialBalance = initialBalance;
  }

  public AccountDTO status(@Nullable Boolean status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable Boolean getStatus() {
    return status;
  }

  public void setStatus(@Nullable Boolean status) {
    this.status = status;
  }

  public AccountDTO clientId(@Nullable Long clientId) {
    this.clientId = clientId;
    return this;
  }

  /**
   * Get clientId
   * @return clientId
   */
  
  @Schema(name = "clientId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientId")
  public @Nullable Long getClientId() {
    return clientId;
  }

  public void setClientId(@Nullable Long clientId) {
    this.clientId = clientId;
  }

  public AccountDTO client(@Nullable ClientDTO client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  @Valid 
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable ClientDTO getClient() {
    return client;
  }

  public void setClient(@Nullable ClientDTO client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountDTO accountDTO = (AccountDTO) o;
    return Objects.equals(this.accountId, accountDTO.accountId) &&
        Objects.equals(this.accountNumber, accountDTO.accountNumber) &&
        Objects.equals(this.accountType, accountDTO.accountType) &&
        Objects.equals(this.initialBalance, accountDTO.initialBalance) &&
        Objects.equals(this.status, accountDTO.status) &&
        Objects.equals(this.clientId, accountDTO.clientId) &&
        Objects.equals(this.client, accountDTO.client);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountNumber, accountType, initialBalance, status, clientId, client);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountDTO {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    initialBalance: ").append(toIndentedString(initialBalance)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    client: ").append(toIndentedString(client)).append("\n");
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

