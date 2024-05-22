package com.example.mlservice.elastic.book;

import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.city.City;

import java.util.List;

public interface CityRepository {

    List<City> findAll();
    List<KnnResult<City>> findNearestNeighbors(int size, List<Float> vectorizedQuestion);
    void insert(City city);

}
