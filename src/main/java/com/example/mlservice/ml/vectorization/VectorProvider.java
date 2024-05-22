package com.example.mlservice.ml.vectorization;

public interface VectorProvider<Request, Response> {

    Response getVector(Request apiRequest);

}
