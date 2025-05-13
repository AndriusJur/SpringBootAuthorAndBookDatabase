package com.andrius.database;
import com.andrius.database.domain.Author;
import com.andrius.database.domain.Book;

public final class TestDataUtil {
    private TestDataUtil(){

    }

    public static Author createTestAuthorA() {
        return Author.builder()
                .name("Firstname Lastname")
                .age(80)
                .build();
    }
    public static Author createTestAuthorB() {
        return Author.builder()
                .name("Vardenis Pavardenis")
                .age(44)
                .build();
    }
    public static Author createTestAuthorC() {
        return Author.builder()
                .name("Jonas Jonauskas")
                .age(24)
                .build();
    }

    public static Book createTestBookA(final Author author) {
        return Book.builder()
                .isbn("1617292540")
                .title("Spring Boot in Action")
                .author(Author.builder()
                        .id(author.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }
    public static Book createTestBookB(final Author author) {
        return Book.builder()
                .isbn("123456789")
                .title("Crossword Puzzles")
                .author(Author.builder()
                        .id(author.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }
    public static Book createTestBookC(final Author author) {
        return Book.builder()
                .isbn("161729442540")
                .title("Puzzles Without Crosswords")
                .author(Author.builder()
                        .id(author.getId()) // use the generated ID
                        .name("Firstname Lastname")
                        .age(80)
                        .build())
                .build();
    }
}
