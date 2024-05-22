package com.example.mlservice.elastic.city;

import com.example.mlservice.elastic.ElasticsearchRepository;
import com.example.mlservice.model.book.BookVectorized;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookVectorizedRepositoryImpl implements BookVectorizedRepository {

    private final ElasticsearchRepository<BookVectorized> cityVectorizedElasticsearchRepository;

    public BookVectorizedRepositoryImpl(
            ElasticsearchRepository<BookVectorized> cityVectorizedElasticsearchRepository) {
        this.cityVectorizedElasticsearchRepository = cityVectorizedElasticsearchRepository;
    }

    @Override
    public void insert(BookVectorized bookVectorized, String index) {
        try {
            cityVectorizedElasticsearchRepository.insert(bookVectorized, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
