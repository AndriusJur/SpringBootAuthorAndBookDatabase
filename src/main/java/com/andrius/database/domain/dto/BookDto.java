package com.andrius.database.domain.dto;

import com.andrius.database.domain.entities.AuthorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class BookDto {

    private String isbn;

    private String title;

    private AuthorDto author;


}
