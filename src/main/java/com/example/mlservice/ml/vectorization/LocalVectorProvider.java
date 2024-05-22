package com.example.mlservice.ml.vectorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LocalVectorProvider
        implements VectorProvider<LocalVectorRequest, LocalVectorResponse> {

    private final RestTemplate restTemplate;
    private final String vectorizationApiUrl;

    public LocalVectorProvider(
            RestTemplate restTemplate,
            @Value("${vector.api.url}") String vectorizationApiUrl) {
        this.restTemplate = restTemplate;
        this.vectorizationApiUrl = vectorizationApiUrl;
    }

    @Override
    public LocalVectorResponse getVector(LocalVectorRequest apiRequest) {
        return restTemplate.postForEntity(
                vectorizationApiUrl,
                apiRequest,
                LocalVectorResponse.class)
                .getBody();
    }
}
