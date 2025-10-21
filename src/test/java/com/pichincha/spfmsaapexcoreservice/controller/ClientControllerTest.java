package com.pichincha.spfmsaapexcoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.model.ClientDTO;
import com.pichincha.spfmsaapexcoreservice.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias del endpoint POST /clients
 * Endpoint crítico: Crea nuevos clientes con validación de datos
 */
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        // Setup test client
        client = new Client();
        client.setPersonId(1L);
        client.setName("Jose Lema");
        client.setGender("M");
        client.setAge(30);
        client.setIdentification("1234567890");
        client.setAddress("Otavalo sn y principal");
        client.setPhone("098254785");
        client.setPassword("1234");
        client.setStatus(true);
    }

    @Test
    void createClient_ValidData_ShouldReturnCreatedClient() throws Exception {
        // Given
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Jose Lema");
        clientDTO.setGender("M");
        clientDTO.setAge(30);
        clientDTO.setIdentification("1234567890");
        clientDTO.setAddress("Otavalo sn y principal");
        clientDTO.setPhone("098254785");
        clientDTO.setPassword("1234");
        clientDTO.setStatus(true);

        // When & Then
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Jose Lema")))
            .andExpect(jsonPath("$.identification", is("1234567890")))
            .andExpect(jsonPath("$.status", is(true)));
    }
}
