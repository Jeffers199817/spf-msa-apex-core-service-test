package com.pichincha.spfmsaapexcoreservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.pichincha.spfmsaapexcoreservice.model.ReportDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReportResponseDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-21T22:09:14.182490100-05:00[America/Bogota]", comments = "Generator version: 7.15.0")
public class ReportResponseDTO {

  @Valid
  private List<@Valid ReportDTO> reportJson = new ArrayList<>();

  private @Nullable String pdfBase64;

  public ReportResponseDTO reportJson(List<@Valid ReportDTO> reportJson) {
    this.reportJson = reportJson;
    return this;
  }

  public ReportResponseDTO addReportJsonItem(ReportDTO reportJsonItem) {
    if (this.reportJson == null) {
      this.reportJson = new ArrayList<>();
    }
    this.reportJson.add(reportJsonItem);
    return this;
  }

  /**
   * Report data in JSON format
   * @return reportJson
   */
  @Valid 
  @Schema(name = "reportJson", description = "Report data in JSON format", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reportJson")
  public List<@Valid ReportDTO> getReportJson() {
    return reportJson;
  }

  public void setReportJson(List<@Valid ReportDTO> reportJson) {
    this.reportJson = reportJson;
  }

  public ReportResponseDTO pdfBase64(@Nullable String pdfBase64) {
    this.pdfBase64 = pdfBase64;
    return this;
  }

  /**
   * PDF report encoded in base64 format
   * @return pdfBase64
   */
  
  @Schema(name = "pdfBase64", description = "PDF report encoded in base64 format", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pdfBase64")
  public @Nullable String getPdfBase64() {
    return pdfBase64;
  }

  public void setPdfBase64(@Nullable String pdfBase64) {
    this.pdfBase64 = pdfBase64;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportResponseDTO reportResponseDTO = (ReportResponseDTO) o;
    return Objects.equals(this.reportJson, reportResponseDTO.reportJson) &&
        Objects.equals(this.pdfBase64, reportResponseDTO.pdfBase64);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportJson, pdfBase64);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportResponseDTO {\n");
    sb.append("    reportJson: ").append(toIndentedString(reportJson)).append("\n");
    sb.append("    pdfBase64: ").append(toIndentedString(pdfBase64)).append("\n");
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

