package com.cooperative.pollsystem.service.cpfvalidator;

import com.cooperative.pollsystem.dto.cpfvalidator.CpfValidatorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/users")
public interface CpfValidatorClient {
    @GetExchange("/{cpf}")
    CpfValidatorResponse validateCpf(@PathVariable("cpf") String cpf);
}
