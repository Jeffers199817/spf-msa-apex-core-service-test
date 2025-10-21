package com.pichincha.spfmsaapexcoreservice.domain;

import com.pichincha.spfmsaapexcoreservice.domain.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id")
  private Long transactionId;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false, length = 20)
  private TransactionType transactionType;

  @Column(name = "amount", nullable = false)
  private Double amount;

  @Column(name = "balance", nullable = false)
  private Double balance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;
}
