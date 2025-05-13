package com.andrius.database.repositories;

import com.andrius.database.domain.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity,String> { //id type of the book is isbn - string
}
