package com.cooperative.pollsystem.config.cpfvalidator;

import com.cooperative.pollsystem.service.cpfvalidator.CpfValidatorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class CpfValidatorBeanConfig {
    @Value("${cpf-validator.base-url}")
    private String baseUrl;

    @Bean
    CpfValidatorClient cpfValidatorClient(RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        HttpServiceProxyFactory clientFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return clientFactory.createClient(CpfValidatorClient.class);
    }
}
