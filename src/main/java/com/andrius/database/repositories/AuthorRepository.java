package com.andrius.database.repositories;

import com.andrius.database.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  //similiar to @Component  + describes this as a repo
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long > {

//    Iterable<AuthorEntity> ageLessThan(int i);
//
//    @Query("SELECT a from Author a where a.age >?1")//using HQL to help JPA
//    Iterable<AuthorEntity> findAuthorsWithAgeGreaterThan(int i);
}
