package com.pichincha.spfmsaapexcoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import com.pichincha.spfmsaapexcoreservice.domain.enums.AccountType;
import com.pichincha.spfmsaapexcoreservice.domain.enums.TransactionType;
import com.pichincha.spfmsaapexcoreservice.exception.InsufficientBalanceException;
import com.pichincha.spfmsaapexcoreservice.model.TransactionDTO;
import com.pichincha.spfmsaapexcoreservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias del endpoint POST /transactions
 * Endpoint crítico: Valida saldo, crea transacciones y actualiza balances
 */
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private Transaction transaction;
    private Account account;
    private Client client;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper for Java 8 date/time
        objectMapper.registerModule(new JavaTimeModule());

        // Setup test client
        client = new Client();
        client.setPersonId(1L);
        client.setName("Jose Lema");
        client.setIdentification("1234567890");
        client.setPassword("1234");
        client.setStatus(true);

        // Setup test account
        account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber("478758");
        account.setAccountType(AccountType.SAVINGS);
        account.setInitialBalance(2000.0);
        account.setStatus(true);
        account.setClient(client);

        // Setup test transaction
        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(575.0);
        transaction.setBalance(2575.0);  // 2000 + 575
        transaction.setAccount(account);
    }

    @Test
    void createTransaction_Deposit_ShouldReturnCreatedTransaction() throws Exception {
        // Given
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDate(OffsetDateTime.now());
        transactionDTO.setTransactionType(TransactionDTO.TransactionTypeEnum.DEPOSIT);
        transactionDTO.setAmount(575.0);
        transactionDTO.setAccountId(1L);

        // When & Then
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount", is(575.0)))
            .andExpect(jsonPath("$.balance", is(2575.0)))
            .andExpect(jsonPath("$.transactionType", is("DEPOSIT")));
    }

    @Test
    void createTransaction_Withdrawal_ShouldReturnCreatedTransaction() throws Exception {
        // Given
        Transaction withdrawal = new Transaction();
        withdrawal.setTransactionId(2L);
        withdrawal.setDate(LocalDateTime.now());
        withdrawal.setTransactionType(TransactionType.WITHDRAWAL);
        withdrawal.setAmount(-575.0);  // Negative amount for withdrawal
        withdrawal.setBalance(1425.0);  // 2000 - 575
        withdrawal.setAccount(account);

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(withdrawal);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDate(OffsetDateTime.now());
        transactionDTO.setTransactionType(TransactionDTO.TransactionTypeEnum.WITHDRAWAL);
        transactionDTO.setAmount(575.0);  // Positive in DTO
        transactionDTO.setAccountId(1L);

        // When & Then
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount", is(-575.0)))  // Negative in response
            .andExpect(jsonPath("$.balance", is(1425.0)))
            .andExpect(jsonPath("$.transactionType", is("WITHDRAWAL")));
    }

    @Test
    void createTransaction_InsufficientBalance_ShouldReturnBadRequest() throws Exception {
        // Given - Trying to withdraw more than available balance
        when(transactionService.createTransaction(any(Transaction.class)))
            .thenThrow(new InsufficientBalanceException("Saldo no disponible"));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDate(OffsetDateTime.now());
        transactionDTO.setTransactionType(TransactionDTO.TransactionTypeEnum.WITHDRAWAL);
        transactionDTO.setAmount(3000.0);  // More than account balance (2000)
        transactionDTO.setAccountId(1L);

        // When & Then
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }
}
