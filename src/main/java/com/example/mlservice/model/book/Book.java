package com.example.mlservice.model.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(String name, String author, @JsonProperty("short_description") String shortDescription) {}
