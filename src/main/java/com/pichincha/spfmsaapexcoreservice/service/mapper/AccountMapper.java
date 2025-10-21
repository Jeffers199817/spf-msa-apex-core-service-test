package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.model.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface AccountMapper {

  @Mapping(source = "client.personId", target = "clientId")
  AccountDTO toDto(Account account);

  @Mapping(source = "clientId", target = "client.personId")
  @Mapping(target = "transactions", ignore = true)
  Account toEntity(AccountDTO accountDTO);
}
