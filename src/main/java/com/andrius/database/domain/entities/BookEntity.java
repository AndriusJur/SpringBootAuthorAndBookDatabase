package com.andrius.database.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity // file to be used w Spring Data JPA
@Table(name="books") // which table maps to in database

public class BookEntity {

    @Id //no generator, manual sequence insertion
    private String isbn;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)//persistent changes in database of Author obj
    @JoinColumn(name =  "author_id")//join column
    private AuthorEntity authorEntity;// due to JPA usage Author obj can be used

}
