package com.example.crud.service;

import com.example.crud.domain.address.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class CepSearch {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CepSearch(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Address cepSearch(String cep) {

        validateCep(cep);

        String url = "https://viacep.com.br/ws/{cep}/json/";

        Map<String, String> uriVariables = Map.of("cep", cep);

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class, uriVariables);

        try {
            Address address = objectMapper.readValue(response.getBody(), Address.class);

            // ViaCEP retorna {"erro": true}
            if (response.getBody().contains("\"erro\": true")) {
                throw new IllegalArgumentException("CEP não encontrado");
            }

            return address;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter resposta do CEP", e);
        }
    }

    private void validateCep(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos numéricos");
        }
    }
}
