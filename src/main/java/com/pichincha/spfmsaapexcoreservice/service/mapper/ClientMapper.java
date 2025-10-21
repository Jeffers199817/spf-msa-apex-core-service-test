package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.model.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

  @Mapping(source = "personId", target = "clientId")
  ClientDTO toDto(Client client);

  @Mapping(source = "clientId", target = "personId")
  @Mapping(target = "accounts", ignore = true)
  Client toEntity(ClientDTO clientDTO);
}
