package com.example.mlservice.controller;

import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.city.City;
import com.example.mlservice.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/get_all")
    public List<City> getAllCities() {
        return cityService.findAllCities();
    }

    @PostMapping("/vectorize_all")
    public void vectorizeAll() {
        cityService.vectorizeAllCities();
    }

    @PostMapping("/find_semantic")
    public List<KnnResult<City>> getCitiesSemantic(@RequestBody String question) {
        return cityService.findCitiesSemantic(question);
    }

}
