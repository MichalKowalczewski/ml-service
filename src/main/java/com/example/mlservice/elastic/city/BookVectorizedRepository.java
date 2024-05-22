package com.example.mlservice.elastic.city;

import com.example.mlservice.model.book.BookVectorized;

public interface BookVectorizedRepository {

    void insert(BookVectorized bookVectorized, String index);

}
