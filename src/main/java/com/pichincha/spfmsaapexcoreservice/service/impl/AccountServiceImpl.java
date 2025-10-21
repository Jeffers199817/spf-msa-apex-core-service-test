package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.exception.ResourceNotFoundException;
import com.pichincha.spfmsaapexcoreservice.repository.AccountRepository;
import com.pichincha.spfmsaapexcoreservice.repository.ClientRepository;
import com.pichincha.spfmsaapexcoreservice.service.AccountService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pichincha.spfmsaapexcoreservice.domain.Client;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final ClientRepository clientRepository;

  @Override
  @Transactional
  public Account createAccount(Account account) {
    log.info("Creating account: {}", account.getAccountNumber());
    
    if (account.getClient() != null && account.getClient().getPersonId() != null) {
      Long clientId = account.getClient().getPersonId();
      Client client = clientRepository.findById(clientId)
        .orElseThrow(() -> {
          log.error("Client not found with ID: {}", clientId);
          return new ResourceNotFoundException("Client not found with id: " + clientId);
        });
      account.setClient(client);
    }
    
    Account savedAccount = accountRepository.save(account);
    log.info("Account created with ID: {}", savedAccount.getAccountId());
    return savedAccount;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Account> findAccountById(Long accountId) {
    return accountRepository.findById(accountId);
  }

  @Override
  @Transactional
  public Account updateAccount(Long accountId, Account account) {
    log.info("Updating account ID: {}", accountId);
    
    return accountRepository.findById(accountId)
      .map(existingAccount -> {
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setInitialBalance(account.getInitialBalance());
        existingAccount.setStatus(account.getStatus());
        
        if (account.getClient() != null) {
          existingAccount.setClient(account.getClient());
        }
        
        Account updatedAccount = accountRepository.save(existingAccount);
        log.info("Account updated: {}", updatedAccount.getAccountId());
        return updatedAccount;
      })
      .orElseThrow(() -> {
        log.error("Account not found with ID: {}", accountId);
        return new ResourceNotFoundException("Account not found with id: " + accountId);
      });
  }

  @Override
  @Transactional
  public void deleteAccount(Long accountId) {
    log.info("Deleting account ID: {}", accountId);
    
    if (!accountRepository.existsById(accountId)) {
      log.error("Account not found with ID: {}", accountId);
      throw new ResourceNotFoundException("Account not found with id: " + accountId);
    }
    
    accountRepository.deleteById(accountId);
  }
}
