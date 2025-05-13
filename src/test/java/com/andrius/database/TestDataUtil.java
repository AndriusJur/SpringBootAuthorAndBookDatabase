package com.andrius.database;
import com.andrius.database.domain.dto.AuthorDto;
import com.andrius.database.domain.dto.BookDto;
import com.andrius.database.domain.entities.AuthorEntity;
import com.andrius.database.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil(){

    }

    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .name("Firstname Lastname")
                .age(80)
                .build();
    }
    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .name("Vardenis Pavardenis")
                .age(44)
                .build();
    }
    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .name("Jonas Jonauskas")
                .age(24)
                .build();
    }

    public static BookEntity createTestBookEntityA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("1617292540")
                .title("Spring Boot in Action")
                .authorEntity(AuthorEntity.builder()
                        .id(authorEntity.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }

    public static BookDto createTestBookDto (final AuthorDto author){
        return BookDto.builder()
                .isbn("1617292540")
                .title("Spring Boot in Action")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123456789")
                .title("Crossword Puzzles")
                .authorEntity(AuthorEntity.builder()
                        .id(authorEntity.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }
    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("161729442540")
                .title("Puzzles Without Crosswords")
                .authorEntity(AuthorEntity.builder()
                        .id(authorEntity.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }
}
