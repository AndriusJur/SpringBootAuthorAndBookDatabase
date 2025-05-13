package com.andrius.database.controllers;

import com.andrius.database.domain.dto.BookDto;
import com.andrius.database.domain.entities.BookEntity;
import com.andrius.database.mappers.Mapper;
import com.andrius.database.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    @Autowired   /// test if working
    private Mapper<BookEntity,BookDto> bookMapper;
    @Autowired
    private BookService bookService;

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn,
                                              @RequestBody BookDto bookDto){ //converts incoming json to bookDto obj
        BookEntity bookEntity = bookMapper.mapFrom(bookDto); // Converts incoming BookDto (API-facing) to BookEntity (database-facing)
        BookEntity savedBook = bookService.createBook(isbn, bookEntity);// Delegates business logic to service layer
        BookDto savedBookEntity = bookMapper.mapTo(savedBook);// Converts persisted BookEntity back to BookDto for API response, bc Prevents exposing database details to clients
        return new ResponseEntity<>(savedBookEntity, HttpStatus.CREATED);
    }
    @GetMapping(path="/books")
    public List<BookDto> listBooks(){
        List<BookEntity>books = bookService.findAll();
        return books.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping (path="/books/{isbn}")
    public ResponseEntity<BookDto> getBook (@PathVariable ("isbn") String isbn){
        Optional<BookEntity> foundBook=bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto=bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
