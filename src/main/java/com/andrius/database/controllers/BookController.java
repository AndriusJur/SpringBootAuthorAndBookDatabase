package com.andrius.database.controllers;

import com.andrius.database.domain.dto.BookDto;
import com.andrius.database.domain.entities.BookEntity;
import com.andrius.database.mappers.Mapper;
import com.andrius.database.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.awt.print.Pageable;
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
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn,
                                                    @RequestBody BookDto bookDto){ //converts incoming json to bookDto obj
        BookEntity bookEntity = bookMapper.mapFrom(bookDto); // Converts incoming BookDto (API-facing) to BookEntity (database-facing)
        boolean bookExists=bookService.isExists(isbn);
        BookEntity savedBook = bookService.createUpdateBook(isbn, bookEntity);// Delegates business logic to service layer
        BookDto savedBookEntity = bookMapper.mapTo(savedBook);// Converts persisted BookEntity back to BookDto for API response, bc Prevents exposing database details to clients

        if(bookExists){      //update

            return new ResponseEntity<>(savedBookEntity, HttpStatus.OK);

        }else{                              //create
            return new ResponseEntity<>(savedBookEntity, HttpStatus.CREATED);
        }
    }
    @GetMapping(path="/books")
    public Page<BookDto> listBooks(Pageable pageable){
        Page<BookEntity>books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping (path="/books/{isbn}")
    public ResponseEntity<BookDto> getBook (@PathVariable ("isbn") String isbn){
        Optional<BookEntity> foundBook=bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto=bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping (path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
          @PathVariable("isbn") String isbn,
          @RequestBody BookDto bookDto
    ){
        boolean bookExists = bookService.isExists(isbn);
        if(!bookExists){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn,bookEntity);
        return new ResponseEntity<>(
                bookMapper.mapTo(updatedBookEntity),
                HttpStatus.OK);
    }
    @DeleteMapping (path = "/books/{isbn}")
    public ResponseEntity deleteBook(
            @PathVariable ("isbn") String isbn
    ){
        bookService.delete(isbn);
        return new ResponseEntity(
                HttpStatus.NO_CONTENT
        );
    }
    @DeleteMapping (path = "/books") // nuclear option,
    public ResponseEntity deleteAllBooks(){
        bookService.delete();
        return new ResponseEntity(
                HttpStatus.NO_CONTENT
        );
    }


}
