package com.example.mlservice.service;

import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.elastic.book.CityRepository;
import com.example.mlservice.elastic.book.CityVectorizedRepository;
import com.example.mlservice.ml.vectorization.LocalVectorRequest;
import com.example.mlservice.ml.vectorization.LocalVectorResponse;
import com.example.mlservice.ml.vectorization.VectorProvider;
import com.example.mlservice.model.city.City;
import com.example.mlservice.model.city.CityVectorized;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityService {

    private final CityRepository cityRepository;
    private final CityVectorizedRepository cityVectorizedRepository;
    private final VectorProvider<LocalVectorRequest, LocalVectorResponse> vectorProvider;

    public CityService(
            CityRepository cityRepository,
            CityVectorizedRepository cityVectorizedRepository,
            VectorProvider<LocalVectorRequest, LocalVectorResponse> vectorProvider) {
        this.cityRepository = cityRepository;
        this.cityVectorizedRepository = cityVectorizedRepository;
        this.vectorProvider = vectorProvider;
    }

    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    public void vectorizeAllCities() {
        List<City> cities = cityRepository.findAll();
        cities.stream()
                .map(city -> new CityVectorized(
                        city.name(),
                        city.country(),
                        provideCityVector(city)))
                .forEach(cityVectorizedRepository::insert);
    }

    public List<KnnResult<City>> findCitiesSemantic(String question) {
        return cityRepository.findNearestNeighbors(10,
                        provideVectorizedQuestion(question));
    }

    private static City mapVectorizedCityToCity(CityVectorized cityVectorized) {
        return new City(cityVectorized.name(), cityVectorized.country());
    }

    private List<Float> provideCityVector(City city) {
        return vectorProvider.getVector(prepareCityDataToVectorization(city))
                .vectorizedData().get(0);
    }

    private List<Float> provideVectorizedQuestion(String question) {
        return vectorProvider.getVector(new LocalVectorRequest(List.of(question)))
                .vectorizedData().get(0);
    }

    private LocalVectorRequest prepareCityDataToVectorization(City city) {
        return new LocalVectorRequest(List.of(city.name() + " " + city.country()));
    }
}
