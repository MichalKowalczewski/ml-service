package com.example.mlservice.elastic.city;

import com.example.mlservice.elastic.ElasticsearchRepository;
import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.book.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class BookRepositoryImpl implements BookRepository {

    private final ElasticsearchRepository<Book> bookElasticsearchRepository;
    private final String index;

    public BookRepositoryImpl(
            ElasticsearchRepository<Book> bookElasticsearchRepository,
            @Value("${book.index}") String index) {
        this.bookElasticsearchRepository = bookElasticsearchRepository;
        this.index = index;
    }

    @Override
    public List<Book> findAll() {
        try {
            return bookElasticsearchRepository.findAllDocuments(Book.class, index);
        } catch (IOException e) {
            System.out.println(e);
            return List.of();
        }
    }

    public List<Book> findSimpleQuery(String query) {
        try {
            return bookElasticsearchRepository.multiMatchSearch(
                    Book.class,
                    index,
                    10L,
                    query);
        } catch (IOException e) {
            System.out.println(e);
            return List.of();
        }
    }

    @Override
    public List<KnnResult<Book>> findNearestNeighbors(int size, List<Float> vectorizedQuestion, String index) {
        try {
            return bookElasticsearchRepository.findNearestNeighbors(
                    Book.class,
                    index,
                    size,
                    vectorizedQuestion);
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public void insert(Book book) {
        try {
            bookElasticsearchRepository.insert(book, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
