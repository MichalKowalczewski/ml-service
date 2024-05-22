package com.example.mlservice.elastic.book;

import com.example.mlservice.elastic.ElasticsearchRepository;
import com.example.mlservice.model.city.CityVectorized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CityVectorizedRepositoryImpl implements CityVectorizedRepository {

    private final ElasticsearchRepository<CityVectorized> cityVectorizedElasticsearchRepository;
    private final String index;

    public CityVectorizedRepositoryImpl(
            ElasticsearchRepository<CityVectorized> cityVectorizedElasticsearchRepository,
            @Value("${vectorized.city.index}") String index) {
        this.cityVectorizedElasticsearchRepository = cityVectorizedElasticsearchRepository;
        this.index = index;
    }

    @Override
    public void insert(CityVectorized cityVectorized) {
        try {
            cityVectorizedElasticsearchRepository.insert(cityVectorized, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
