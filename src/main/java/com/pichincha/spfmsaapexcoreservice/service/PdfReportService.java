package com.pichincha.spfmsaapexcoreservice.service;

import com.pichincha.spfmsaapexcoreservice.model.ReportDTO;

import java.util.List;

public interface PdfReportService {

    byte[] generateAccountStatementPdf(List<ReportDTO> reportData);

}
