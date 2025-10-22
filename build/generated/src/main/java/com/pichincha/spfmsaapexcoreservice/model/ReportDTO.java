package com.pichincha.spfmsaapexcoreservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
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
 * ReportDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-21T22:09:14.182490100-05:00[America/Bogota]", comments = "Generator version: 7.15.0")
public class ReportDTO {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private @Nullable String client;

  private @Nullable String accountNumber;

  private @Nullable String type;

  private @Nullable Double initialBalance;

  private @Nullable Boolean status;

  private @Nullable Double movement;

  private @Nullable Double availableBalance;

  private @Nullable Double totalDebits;

  private @Nullable Double totalCredits;

  public ReportDTO date(@Nullable LocalDate date) {
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
  public @Nullable LocalDate getDate() {
    return date;
  }

  public void setDate(@Nullable LocalDate date) {
    this.date = date;
  }

  public ReportDTO client(@Nullable String client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable String getClient() {
    return client;
  }

  public void setClient(@Nullable String client) {
    this.client = client;
  }

  public ReportDTO accountNumber(@Nullable String accountNumber) {
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

  public ReportDTO type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public ReportDTO initialBalance(@Nullable Double initialBalance) {
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

  public ReportDTO status(@Nullable Boolean status) {
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

  public ReportDTO movement(@Nullable Double movement) {
    this.movement = movement;
    return this;
  }

  /**
   * Get movement
   * @return movement
   */
  
  @Schema(name = "movement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("movement")
  public @Nullable Double getMovement() {
    return movement;
  }

  public void setMovement(@Nullable Double movement) {
    this.movement = movement;
  }

  public ReportDTO availableBalance(@Nullable Double availableBalance) {
    this.availableBalance = availableBalance;
    return this;
  }

  /**
   * Get availableBalance
   * @return availableBalance
   */
  
  @Schema(name = "availableBalance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availableBalance")
  public @Nullable Double getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(@Nullable Double availableBalance) {
    this.availableBalance = availableBalance;
  }

  public ReportDTO totalDebits(@Nullable Double totalDebits) {
    this.totalDebits = totalDebits;
    return this;
  }

  /**
   * Total of all debits in the period
   * @return totalDebits
   */
  
  @Schema(name = "totalDebits", description = "Total of all debits in the period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalDebits")
  public @Nullable Double getTotalDebits() {
    return totalDebits;
  }

  public void setTotalDebits(@Nullable Double totalDebits) {
    this.totalDebits = totalDebits;
  }

  public ReportDTO totalCredits(@Nullable Double totalCredits) {
    this.totalCredits = totalCredits;
    return this;
  }

  /**
   * Total of all credits in the period
   * @return totalCredits
   */
  
  @Schema(name = "totalCredits", description = "Total of all credits in the period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCredits")
  public @Nullable Double getTotalCredits() {
    return totalCredits;
  }

  public void setTotalCredits(@Nullable Double totalCredits) {
    this.totalCredits = totalCredits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportDTO reportDTO = (ReportDTO) o;
    return Objects.equals(this.date, reportDTO.date) &&
        Objects.equals(this.client, reportDTO.client) &&
        Objects.equals(this.accountNumber, reportDTO.accountNumber) &&
        Objects.equals(this.type, reportDTO.type) &&
        Objects.equals(this.initialBalance, reportDTO.initialBalance) &&
        Objects.equals(this.status, reportDTO.status) &&
        Objects.equals(this.movement, reportDTO.movement) &&
        Objects.equals(this.availableBalance, reportDTO.availableBalance) &&
        Objects.equals(this.totalDebits, reportDTO.totalDebits) &&
        Objects.equals(this.totalCredits, reportDTO.totalCredits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, client, accountNumber, type, initialBalance, status, movement, availableBalance, totalDebits, totalCredits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportDTO {\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    client: ").append(toIndentedString(client)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    initialBalance: ").append(toIndentedString(initialBalance)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    movement: ").append(toIndentedString(movement)).append("\n");
    sb.append("    availableBalance: ").append(toIndentedString(availableBalance)).append("\n");
    sb.append("    totalDebits: ").append(toIndentedString(totalDebits)).append("\n");
    sb.append("    totalCredits: ").append(toIndentedString(totalCredits)).append("\n");
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

