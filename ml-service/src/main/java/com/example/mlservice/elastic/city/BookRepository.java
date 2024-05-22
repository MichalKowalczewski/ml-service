package com.example.mlservice.elastic.city;

import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.book.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();
    List<KnnResult<Book>> findNearestNeighbors(int size, List<Float> vectorizedQuestion, String index);
    List<Book> findSimpleQuery(String query);
    void insert(Book book);

}
