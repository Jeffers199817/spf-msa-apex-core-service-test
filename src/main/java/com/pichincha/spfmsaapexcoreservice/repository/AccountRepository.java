package com.pichincha.spfmsaapexcoreservice.repository;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  
  @EntityGraph(attributePaths = {"client", "transactions"})
  @NonNull
  @Override
  List<Account> findAll();
  
  @EntityGraph(attributePaths = {"client", "transactions"})
  @NonNull
  @Override
  Optional<Account> findById(@NonNull Long id);
}
