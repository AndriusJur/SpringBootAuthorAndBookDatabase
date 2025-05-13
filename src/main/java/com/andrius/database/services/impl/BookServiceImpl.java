package com.andrius.database.services.impl;

import com.andrius.database.domain.entities.BookEntity;
import com.andrius.database.repositories.BookRepository;
import com.andrius.database.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookEntity createBook(String isbn, BookEntity book) {
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
}
