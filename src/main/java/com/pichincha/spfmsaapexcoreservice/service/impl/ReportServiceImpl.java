package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import com.pichincha.spfmsaapexcoreservice.repository.TransactionRepository;
import com.pichincha.spfmsaapexcoreservice.service.ReportService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final TransactionRepository transactionRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Transaction> generateAccountStatement(
    Long clientId,
    LocalDate startDate,
    LocalDate endDate
  ) {
    log.info("Generating report for client ID: {} from {} to {}", clientId, startDate, endDate);
    
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
    
    List<Transaction> transactions = transactionRepository.findByClientAndDateRange(
      clientId,
      startDateTime,
      endDateTime
    );
    
    log.info("Report generated: {} transactions found", transactions.size());
    return transactions;
  }
}
