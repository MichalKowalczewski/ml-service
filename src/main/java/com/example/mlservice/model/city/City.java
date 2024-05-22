package com.example.mlservice.model.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record City(String name, String country) {

}
