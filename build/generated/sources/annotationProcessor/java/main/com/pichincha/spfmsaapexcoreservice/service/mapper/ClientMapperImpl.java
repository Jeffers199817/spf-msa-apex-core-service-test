package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.model.ClientDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDTO toDto(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setClientId( client.getPersonId() );
        clientDTO.setName( client.getName() );
        clientDTO.setGender( client.getGender() );
        clientDTO.setAge( client.getAge() );
        clientDTO.setIdentification( client.getIdentification() );
        clientDTO.setAddress( client.getAddress() );
        clientDTO.setPhone( client.getPhone() );
        clientDTO.setPassword( client.getPassword() );
        clientDTO.setStatus( client.getStatus() );

        return clientDTO;
    }

    @Override
    public Client toEntity(ClientDTO clientDTO) {
        if ( clientDTO == null ) {
            return null;
        }

        Client client = new Client();

        client.setPersonId( clientDTO.getClientId() );
        client.setName( clientDTO.getName() );
        client.setGender( clientDTO.getGender() );
        client.setAge( clientDTO.getAge() );
        client.setIdentification( clientDTO.getIdentification() );
        client.setAddress( clientDTO.getAddress() );
        client.setPhone( clientDTO.getPhone() );
        client.setPassword( clientDTO.getPassword() );
        client.setStatus( clientDTO.getStatus() );

        return client;
    }
}
