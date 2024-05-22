package com.example.mlservice.elastic;

public record KnnResult<T>(T result, Double score) {
}
