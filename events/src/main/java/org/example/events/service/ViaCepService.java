package org.example.events.service;

import org.example.events.entity.ViaCep;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "viaCep", url = "https://viacep.com.br/ws/")
public interface ViaCepService {

    @GetMapping("{cep}/json")
    ViaCep getCepInfo(@PathVariable("cep") String cep);
}
