package com.pichincha.spfmsaapexcoreservice.service;

import com.pichincha.spfmsaapexcoreservice.domain.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {

  Client createClient(Client client);

  List<Client> getAllClients();

  Optional<Client> findClientById(Long clientId);

  Client updateClient(Long clientId, Client client);

  void deleteClient(Long clientId);
}
