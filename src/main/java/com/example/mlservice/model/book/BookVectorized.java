package com.example.mlservice.model.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record BookVectorized(String name, String author, @JsonProperty("short_description") String shortDescription, List<Float> vector) {
}
