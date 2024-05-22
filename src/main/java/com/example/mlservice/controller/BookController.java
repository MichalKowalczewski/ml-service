package com.example.mlservice.controller;

import com.example.mlservice.config.Views;
import com.example.mlservice.elastic.KnnResult;
import com.example.mlservice.model.book.Book;
import com.example.mlservice.model.book.BookVectorized;
import com.example.mlservice.service.BookService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/get_all")
    public List<Book> getAllBooks() {
        return bookService.findAllCities();
    }

    @PostMapping("/vectorize_all_name")
    public void vectorizeAllByName() {
        bookService.vectorizeAllBooksByName();
    }

    @PostMapping("/vectorize_all_author")
    public void vectorizeAllByAuthor() {
        bookService.vectorizeAllBooksByBookAuthor();
    }

    @PostMapping("/vectorize_all")
    public void vectorizeAllFileds() {
        bookService.vectorizeAllBooksByAllFields();
    }


    @PostMapping("/find_semantic_name")
    public List<KnnResult<Book>> getBooksSemanticName(@RequestBody String question) {
        return bookService.findBooksSemanticName(question);
    }

    @PostMapping("/find_semantic_author")
    public List<KnnResult<Book>> getBooksSemanticAuthor(@RequestBody String question) {
        return bookService.findBooksSemanticAuthor(question);
    }

    @PostMapping("/find_semantic_all")
    public List<KnnResult<Book>> getBooksSemanticAll(@RequestBody String question) {
        return bookService.findBooksSemanticAll(question);
    }

    @PostMapping("/find_simple")
    public List<Book> getBooksSimple(@RequestBody String question) {
        return bookService.findSimpleQuery(question);
    }

}
