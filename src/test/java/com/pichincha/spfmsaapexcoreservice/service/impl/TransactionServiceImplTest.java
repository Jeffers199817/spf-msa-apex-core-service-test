package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import com.pichincha.spfmsaapexcoreservice.domain.enums.AccountType;
import com.pichincha.spfmsaapexcoreservice.domain.enums.TransactionType;
import com.pichincha.spfmsaapexcoreservice.exception.InsufficientBalanceException;
import com.pichincha.spfmsaapexcoreservice.exception.ResourceNotFoundException;
import com.pichincha.spfmsaapexcoreservice.repository.AccountRepository;
import com.pichincha.spfmsaapexcoreservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionServiceImpl
 * Testing business logic and balance calculations
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;
    private Client client;

    @BeforeEach
    void setUp() {
        // Setup test client
        client = new Client();
        client.setPersonId(1L);
        client.setName("Jose Lema");
        client.setIdentification("1234567890");
        client.setPassword("1234");
        client.setStatus(true);

        // Setup test account with initial balance 1000
        account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber("478758");
        account.setAccountType(AccountType.SAVINGS);
        account.setInitialBalance(1000.0);
        account.setStatus(true);
        account.setClient(client);
        account.setTransactions(new ArrayList<>());
    }

    @Test
    void createTransaction_Deposit_ShouldIncreaseBalance() {
        // Given
        Transaction deposit = new Transaction();
        deposit.setAccount(account);
        deposit.setTransactionType(TransactionType.DEPOSIT);
        deposit.setAmount(500.0);
        deposit.setDate(LocalDateTime.now());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction saved = invocation.getArgument(0);
            saved.setTransactionId(1L);
            return saved;
        });

        // When
        Transaction result = transactionService.createTransaction(deposit);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(1500.0);  // 1000 + 500
        assertThat(result.getAmount()).isEqualTo(500.0);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_Withdrawal_ShouldDecreaseBalance() {
        // Given
        Transaction withdrawal = new Transaction();
        withdrawal.setAccount(account);
        withdrawal.setTransactionType(TransactionType.WITHDRAWAL);
        withdrawal.setAmount(300.0);  // Positive amount
        withdrawal.setDate(LocalDateTime.now());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction saved = invocation.getArgument(0);
            saved.setTransactionId(1L);
            return saved;
        });

        // When
        Transaction result = transactionService.createTransaction(withdrawal);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(700.0);  // 1000 - 300
        assertThat(result.getAmount()).isEqualTo(-300.0);  // Converted to negative
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WithInsufficientBalance_ShouldThrowException() {
        // Given
        Transaction withdrawal = new Transaction();
        withdrawal.setAccount(account);
        withdrawal.setTransactionType(TransactionType.WITHDRAWAL);
        withdrawal.setAmount(1500.0);  // More than account balance (1000)
        withdrawal.setDate(LocalDateTime.now());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(withdrawal))
            .isInstanceOf(InsufficientBalanceException.class)
            .hasMessageContaining("Saldo no disponible");

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WithNonExistentAccount_ShouldThrowException() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(100.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(transaction))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Account not found with id: 1");

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WithNullAccount_ShouldThrowException() {
        // Given
        Transaction transaction = new Transaction();
        transaction.setAccount(null);  // No account
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(100.0);

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(transaction))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Account is required for transaction");

        verify(accountRepository, never()).findById(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WithPreviousTransactions_ShouldCalculateCorrectBalance() {
        // Given
        // Add previous transaction to account
        Transaction previousTransaction = new Transaction();
        previousTransaction.setTransactionId(1L);
        previousTransaction.setAmount(200.0);
        previousTransaction.setBalance(1200.0);  // 1000 + 200
        account.getTransactions().add(previousTransaction);

        Transaction newWithdrawal = new Transaction();
        newWithdrawal.setAccount(account);
        newWithdrawal.setTransactionType(TransactionType.WITHDRAWAL);
        newWithdrawal.setAmount(400.0);
        newWithdrawal.setDate(LocalDateTime.now());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction saved = invocation.getArgument(0);
            saved.setTransactionId(2L);
            return saved;
        });

        // When
        Transaction result = transactionService.createTransaction(newWithdrawal);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(800.0);  // 1200 - 400
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_ShouldRecalculateSubsequentBalances() {
        // Given
        LocalDateTime baseDate = LocalDateTime.now().minusDays(3);
        
        // Setup 3 transactions
        Transaction trans1 = createTransaction(1L, TransactionType.DEPOSIT, 500.0, 1500.0, baseDate);
        Transaction trans2 = createTransaction(2L, TransactionType.WITHDRAWAL, -200.0, 1300.0, baseDate.plusDays(1));
        Transaction trans3 = createTransaction(3L, TransactionType.DEPOSIT, 300.0, 1600.0, baseDate.plusDays(2));
        
        trans1.setAccount(account);
        trans2.setAccount(account);
        trans3.setAccount(account);

        List<Transaction> allTransactions = Arrays.asList(trans1, trans2, trans3);

        // Update trans2: change withdrawal from 200 to 400
        Transaction updatedTrans2 = new Transaction();
        updatedTrans2.setDate(baseDate.plusDays(1));
        updatedTrans2.setTransactionType(TransactionType.WITHDRAWAL);
        updatedTrans2.setAmount(400.0);  // Changed from 200 to 400

        when(transactionRepository.findById(2L)).thenReturn(Optional.of(trans2));
        when(transactionRepository.findByAccountOrderByDateAsc(account)).thenReturn(allTransactions);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Transaction result = transactionService.updateTransaction(2L, updatedTrans2);

        // Then
        assertThat(result).isNotNull();
        // Trans2 new balance: 1500 - 400 = 1100
        assertThat(trans2.getBalance()).isEqualTo(1100.0);
        // Trans3 should be recalculated: 1100 + 300 = 1400
        assertThat(trans3.getBalance()).isEqualTo(1400.0);
        
        verify(transactionRepository, times(1)).findById(2L);
        verify(transactionRepository, atLeast(2)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_ShouldRecalculateSubsequentBalances() {
        // Given
        LocalDateTime baseDate = LocalDateTime.now().minusDays(2);
        
        Transaction trans1 = createTransaction(1L, TransactionType.DEPOSIT, 500.0, 1500.0, baseDate);
        Transaction trans2 = createTransaction(2L, TransactionType.WITHDRAWAL, -300.0, 1200.0, baseDate.plusDays(1));
        Transaction trans3 = createTransaction(3L, TransactionType.DEPOSIT, 200.0, 1400.0, baseDate.plusDays(2));
        
        trans1.setAccount(account);
        trans2.setAccount(account);
        trans3.setAccount(account);

        List<Transaction> allTransactions = Arrays.asList(trans1, trans2, trans3);

        when(transactionRepository.findById(2L)).thenReturn(Optional.of(trans2));
        when(transactionRepository.findByAccountOrderByDateAsc(account)).thenReturn(allTransactions);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When - Delete trans2
        transactionService.deleteTransaction(2L);

        // Then
        // Trans3 should be recalculated based on trans1: 1500 + 200 = 1700
        assertThat(trans3.getBalance()).isEqualTo(1700.0);
        
        verify(transactionRepository, times(1)).deleteById(2L);
        verify(transactionRepository, atLeast(1)).save(trans3);
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        // Given
        List<Transaction> transactions = Arrays.asList(
            createTransaction(1L, TransactionType.DEPOSIT, 100.0, 1100.0, LocalDateTime.now()),
            createTransaction(2L, TransactionType.WITHDRAWAL, -50.0, 1050.0, LocalDateTime.now())
        );
        
        when(transactionRepository.findAll()).thenReturn(transactions);

        // When
        List<Transaction> result = transactionService.getAllTransactions();

        // Then
        assertThat(result).hasSize(2);
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void findTransactionById_WithExistingId_ShouldReturnTransaction() {
        // Given
        Transaction transaction = createTransaction(1L, TransactionType.DEPOSIT, 100.0, 1100.0, LocalDateTime.now());
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // When
        Optional<Transaction> result = transactionService.findTransactionById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTransactionId()).isEqualTo(1L);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void findTransactionById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Transaction> result = transactionService.findTransactionById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(transactionRepository, times(1)).findById(999L);
    }

    // Helper method to create test transactions
    private Transaction createTransaction(Long id, TransactionType type, Double amount, Double balance, LocalDateTime date) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setBalance(balance);
        transaction.setDate(date);
        return transaction;
    }
}
