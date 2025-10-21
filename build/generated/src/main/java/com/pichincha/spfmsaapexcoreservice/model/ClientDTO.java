package com.pichincha.spfmsaapexcoreservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ClientDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-20T02:43:05.386267500-05:00[America/Bogota]", comments = "Generator version: 7.15.0")
public class ClientDTO {

  private @Nullable Long clientId;

  private @Nullable String name;

  private @Nullable String gender;

  private @Nullable Integer age;

  private @Nullable String identification;

  private @Nullable String address;

  private @Nullable String phone;

  private @Nullable String password;

  private @Nullable Boolean status;

  public ClientDTO clientId(@Nullable Long clientId) {
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

  public ClientDTO name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ClientDTO gender(@Nullable String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
   */
  
  @Schema(name = "gender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gender")
  public @Nullable String getGender() {
    return gender;
  }

  public void setGender(@Nullable String gender) {
    this.gender = gender;
  }

  public ClientDTO age(@Nullable Integer age) {
    this.age = age;
    return this;
  }

  /**
   * Get age
   * @return age
   */
  
  @Schema(name = "age", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("age")
  public @Nullable Integer getAge() {
    return age;
  }

  public void setAge(@Nullable Integer age) {
    this.age = age;
  }

  public ClientDTO identification(@Nullable String identification) {
    this.identification = identification;
    return this;
  }

  /**
   * Get identification
   * @return identification
   */
  
  @Schema(name = "identification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("identification")
  public @Nullable String getIdentification() {
    return identification;
  }

  public void setIdentification(@Nullable String identification) {
    this.identification = identification;
  }

  public ClientDTO address(@Nullable String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
   */
  
  @Schema(name = "address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public @Nullable String getAddress() {
    return address;
  }

  public void setAddress(@Nullable String address) {
    this.address = address;
  }

  public ClientDTO phone(@Nullable String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   */
  
  @Schema(name = "phone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone")
  public @Nullable String getPhone() {
    return phone;
  }

  public void setPhone(@Nullable String phone) {
    this.phone = phone;
  }

  public ClientDTO password(@Nullable String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @Schema(name = "password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public @Nullable String getPassword() {
    return password;
  }

  public void setPassword(@Nullable String password) {
    this.password = password;
  }

  public ClientDTO status(@Nullable Boolean status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientDTO clientDTO = (ClientDTO) o;
    return Objects.equals(this.clientId, clientDTO.clientId) &&
        Objects.equals(this.name, clientDTO.name) &&
        Objects.equals(this.gender, clientDTO.gender) &&
        Objects.equals(this.age, clientDTO.age) &&
        Objects.equals(this.identification, clientDTO.identification) &&
        Objects.equals(this.address, clientDTO.address) &&
        Objects.equals(this.phone, clientDTO.phone) &&
        Objects.equals(this.password, clientDTO.password) &&
        Objects.equals(this.status, clientDTO.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, name, gender, age, identification, address, phone, password, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientDTO {\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    identification: ").append(toIndentedString(identification)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

