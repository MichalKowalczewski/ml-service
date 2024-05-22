package com.example.mlservice.elastic.book;

import com.example.mlservice.elastic.ElasticsearchRepository;
import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.city.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CityRepositoryImpl implements CityRepository {

    private final ElasticsearchRepository<City> cityElasticsearchRepository;
    private final String index;

    public CityRepositoryImpl(
            ElasticsearchRepository<City> cityElasticsearchRepository,
            @Value("${book.index}") String index) {
        this.cityElasticsearchRepository = cityElasticsearchRepository;
        this.index = index;
    }

    @Override
    public List<City> findAll() {
        try {
            return cityElasticsearchRepository.findAllDocuments(City.class, index);
        } catch (IOException e) {
            System.out.println(e);
            return List.of();
        }
    }

    @Override
    public List<KnnResult<City>> findNearestNeighbors(int size, List<Float> vectorizedQuestion) {
        try {
            return cityElasticsearchRepository.findNearestNeighbors(
                    City.class,
                    index,
                    size,
                    vectorizedQuestion);
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public void insert(City city) {
        try {
            cityElasticsearchRepository.insert(city, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
