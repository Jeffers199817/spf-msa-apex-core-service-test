package com.pichincha.spfmsaapexcoreservice.service;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {

  Account createAccount(Account account);

  List<Account> getAllAccounts();

  Optional<Account> findAccountById(Long accountId);

  Account updateAccount(Long accountId, Account account);

  void deleteAccount(Long accountId);
}
