package com.example.mlservice.service;

import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.elastic.city.BookRepository;
import com.example.mlservice.elastic.city.BookVectorizedRepository;
import com.example.mlservice.ml.vectorization.LocalVectorRequest;
import com.example.mlservice.ml.vectorization.LocalVectorResponse;
import com.example.mlservice.ml.vectorization.VectorProvider;
import com.example.mlservice.model.book.Book;
import com.example.mlservice.model.book.BookVectorized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookService {

    private final BookRepository bookRepository;
    private final BookVectorizedRepository bookVectorizedRepository;
    private final VectorProvider<LocalVectorRequest, LocalVectorResponse> vectorProvider;
    private final String vectorizedNameBookIndex;
    private final String vectorizedAuthorBookIndex;
    private final String vectorizedBookIndex;

    public BookService(
            BookRepository bookRepository,
            BookVectorizedRepository bookVectorizedRepository,
            VectorProvider<LocalVectorRequest, LocalVectorResponse> vectorProvider,
            @Value("${vectorized.name.book.index}") String vectorizedNameBookIndex,
            @Value("${vectorized.author.book.index}") String vectorizedAuthorBookIndex,
            @Value("${vectorized.book.index}") String vectorizedBookIndex
    ) {
        this.bookRepository = bookRepository;
        this.bookVectorizedRepository = bookVectorizedRepository;
        this.vectorProvider = vectorProvider;
        this.vectorizedNameBookIndex = vectorizedNameBookIndex;
        this.vectorizedAuthorBookIndex = vectorizedAuthorBookIndex;
        this.vectorizedBookIndex = vectorizedBookIndex;
    }

    public List<Book> findAllCities() {
        return bookRepository.findAll();
    }

    public void vectorizeAllBooksByName() {
        List<Book> books = bookRepository.findAll();
        books.stream()
                .map(book -> vectorizeAndMap(book, book.name()))
                .forEach(bookVectorized -> bookVectorizedRepository.insert(bookVectorized, vectorizedNameBookIndex));
    }

    public void vectorizeAllBooksByBookAuthor() {
        List<Book> books = bookRepository.findAll();
        books.stream()
                .map(book -> vectorizeAndMap(book, book.author()))
                .forEach(bookVectorized -> bookVectorizedRepository.insert(bookVectorized, vectorizedAuthorBookIndex));
    }

    public void vectorizeAllBooksByAllFields() {
        List<Book> books = bookRepository.findAll();
        books.stream()
                .map(book -> vectorizeAndMap(book, book.name(), book.author(), book.shortDescription()))
                .forEach(bookVectorized -> bookVectorizedRepository.insert(bookVectorized, vectorizedBookIndex));
    }

    public List<KnnResult<Book>> findBooksSemanticName(String question) {
        return bookRepository.findNearestNeighbors(10,
                        provideVectorizedQuestion(question),
                        vectorizedNameBookIndex);
    }

    public List<KnnResult<Book>>  findBooksSemanticAuthor(String question) {
        return bookRepository.findNearestNeighbors(10,
                        provideVectorizedQuestion(question),
                        vectorizedAuthorBookIndex);
    }

    public List<KnnResult<Book>> findBooksSemanticAll(String question) {
        return bookRepository.findNearestNeighbors(10,
                        provideVectorizedQuestion(question),
                        vectorizedBookIndex);
    }

    public List<Book> findSimpleQuery(String question) {
        return bookRepository.findSimpleQuery(question);
    }

    private BookVectorized vectorizeAndMap(Book book, String... fieldsToBeVectorized) {
        return new BookVectorized(
                book.name(),
                book.author(),
                book.shortDescription(),
                provideBookVector(fieldsToBeVectorized));
    }

    private List<Float> provideBookVector(String... fieldsToBeVectorized) {
        return vectorProvider.getVector(new LocalVectorRequest(List.of(String.join(" ", fieldsToBeVectorized))))
                .vectorizedData().get(0);
    }

    private List<Float> provideVectorizedQuestion(String question) {
        return vectorProvider.getVector(new LocalVectorRequest(List.of(question)))
                .vectorizedData().get(0);
    }
}
