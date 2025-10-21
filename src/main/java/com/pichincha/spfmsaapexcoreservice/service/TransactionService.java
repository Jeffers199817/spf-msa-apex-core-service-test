package com.pichincha.spfmsaapexcoreservice.service;

import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

  Transaction createTransaction(Transaction transaction);

  List<Transaction> getAllTransactions();

  Optional<Transaction> findTransactionById(Long transactionId);

  Transaction updateTransaction(Long transactionId, Transaction transaction);

  void deleteTransaction(Long transactionId);
}
