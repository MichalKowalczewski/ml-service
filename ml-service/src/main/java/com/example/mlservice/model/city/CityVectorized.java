package com.example.mlservice.model.city;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

public record CityVectorized(String name, String country, @JsonProperty(access = WRITE_ONLY) List<Float> vector) {

}
