package com.pichincha.spfmsaapexcoreservice.service.impl;

import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.exception.ResourceNotFoundException;
import com.pichincha.spfmsaapexcoreservice.repository.ClientRepository;
import com.pichincha.spfmsaapexcoreservice.service.ClientService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;

  @Override
  @Transactional
  public Client createClient(Client client) {
    log.info("Creating client: {}", client.getIdentification());
    Client savedClient = clientRepository.save(client);
    log.info("Client created with ID: {}", savedClient.getPersonId());
    return savedClient;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Client> getAllClients() {
    return clientRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Client> findClientById(Long clientId) {
    return clientRepository.findById(clientId);
  }

  @Override
  @Transactional
  public Client updateClient(Long clientId, Client client) {
    log.info("Updating client ID: {}", clientId);
    
    return clientRepository.findById(clientId)
      .map(existingClient -> {
        existingClient.setName(client.getName());
        existingClient.setGender(client.getGender());
        existingClient.setAge(client.getAge());
        existingClient.setIdentification(client.getIdentification());
        existingClient.setAddress(client.getAddress());
        existingClient.setPhone(client.getPhone());
        existingClient.setPassword(client.getPassword());
        existingClient.setStatus(client.getStatus());
        
        Client updatedClient = clientRepository.save(existingClient);
        log.info("Client updated: {}", updatedClient.getPersonId());
        return updatedClient;
      })
      .orElseThrow(() -> {
        log.error("Client not found with ID: {}", clientId);
        return new ResourceNotFoundException("Client not found with id: " + clientId);
      });
  }

  @Override
  @Transactional
  public void deleteClient(Long clientId) {
    log.info("Deleting client ID: {}", clientId);
    
    if (!clientRepository.existsById(clientId)) {
      log.error("Client not found with ID: {}", clientId);
      throw new ResourceNotFoundException("Client not found with id: " + clientId);
    }
    
    clientRepository.deleteById(clientId);
  }
}
