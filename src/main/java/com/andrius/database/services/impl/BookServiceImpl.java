package com.andrius.database.services.impl;

import com.andrius.database.domain.entities.BookEntity;
import com.andrius.database.repositories.BookRepository;
import com.andrius.database.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity book) {
        book.setIsbn(isbn); //ensures correct isbn
        return bookRepository.save(book);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport
                .stream(
                        bookRepository.findAll().spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
       return bookRepository.findById(isbn);
    }

    @Override
    public Page <BookEntity> findAll (Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
       return bookRepository.findById(isbn).map(existingBook->{
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepository.save(existingBook);
        }).orElseThrow(()-> new RuntimeException("Book does not exist"));
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }

    @Override
    public void delete() {
        bookRepository.deleteAll();
    }
}
