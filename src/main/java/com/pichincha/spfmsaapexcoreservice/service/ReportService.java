package com.pichincha.spfmsaapexcoreservice.service;

import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

  List<Transaction> generateAccountStatement(Long clientId, LocalDate startDate, LocalDate endDate);
}
