package com.andrius.database.repositories;

import com.andrius.database.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface BookRepository extends CrudRepository<BookEntity,String>,
        PagingAndSortingRepository<BookEntity, String> //enables pagination
{
    Page<BookEntity> findAll(Pageable pageable); //id type of the book is isbn - string
}
