package com.andrius.database.repositories;


import com.andrius.database.TestDataUtil;
import com.andrius.database.domain.entities.AuthorEntity;
import com.andrius.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class BookEntityRepositoryIntegrationTests {


    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity =TestDataUtil.createTestAuthorA();
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(authorEntity);
        BookEntity savedBook = underTest.save(bookEntity);

        Optional<BookEntity>result=underTest.findById(savedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("author.id") // ignore generated ID, testing book not author
                .isEqualTo(savedBook);

    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        BookEntity savedBookA = underTest.save(bookEntityA);

        AuthorEntity managedAuthor = savedBookA.getAuthorEntity();

        BookEntity bookEntityB = TestDataUtil.createTestBookB(managedAuthor);
        BookEntity savedBookB = underTest.save(bookEntityB);

        BookEntity bookEntityC = TestDataUtil.createTestBookC(managedAuthor);
        BookEntity savedBookC = underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(savedBookA, savedBookB, savedBookC);
    }
    @Test
    public void testThatBookCanBeUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);

        BookEntity savedBook = underTest.save(bookEntityA);
        savedBook.setTitle("UPDATED");
        BookEntity updatedBook = underTest.save(savedBook);

        Optional<BookEntity> result = underTest.findById(updatedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("UPDATED");
    }
    @Test
    public void testThatBookCanBeDeleted(){
        AuthorEntity authorEntity =TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookEntityA);

        underTest.deleteById(bookEntityA.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookEntityA.getIsbn());
        assertThat(result).isEmpty();

    }
}
