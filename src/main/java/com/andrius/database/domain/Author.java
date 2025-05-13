package com.andrius.database.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity // file to be used w Spring Data JPA
@Table(name="authors") // which table maps to in database

public class Author {

    @Id //following will be unique identifier
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_iq_seq")//generates sequence
    private Long id;

    private String name;

    private Integer age;
}
