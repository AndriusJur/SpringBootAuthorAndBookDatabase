package com.andrius.database.services;

import com.andrius.database.domain.entities.BookEntity;
import org.springframework.data.domain.Page;

import java.awt.print.Book;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity createUpdateBook(String isbn, BookEntity book);

    List<BookEntity> findAll();

    Optional<BookEntity> findOne(String isbn);

    Page<BookEntity> findAll(Pageable pageable);

    boolean isExists(String isbn);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    void delete(String isbn);

    void delete();
}
