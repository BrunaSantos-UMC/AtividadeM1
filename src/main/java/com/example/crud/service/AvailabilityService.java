package com.example.crud.service;

import com.example.crud.domain.address.Address;
import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    private final ProductRepository repository;
    private final CepSearch cepSearch;

    public AvailabilityService(ProductRepository repository, CepSearch cepSearch) {
        this.repository = repository;
        this.cepSearch = cepSearch;
    }

    public boolean checkAvailability(String cep, String productId) {

        Product product = repository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);

        Address address = cepSearch.cepSearch(cep);

        if (address == null) {
            throw new IllegalArgumentException("CEP inválido");
        }

        return address.getLocalidade()
                .equalsIgnoreCase(product.getDistribution_center());
    }
}