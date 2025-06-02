package controllers;

import com.andrius.database.BooksApiApplication;
import com.andrius.database.TestDataUtil;
import com.andrius.database.domain.dto.BookDto;
import com.andrius.database.domain.entities.AuthorEntity;
import com.andrius.database.domain.entities.BookEntity;
import com.andrius.database.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = BooksApiApplication.class) // force-loading. Explicitly stating the config class if test hierarchy is diferent from source
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc  //Sets up a simulated web environment for testing controllers, autowires

public class BookControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;


    @Test
    public void testThatCreateUpdateBookSuccessfullyReturnsHttp200Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }

    @Test
    public void testThatCreateBookSuccessfullyCreatesUpdatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())

        );

    }

    @Test
    public void testThatListBooksSuccessfullyReturnsHttp200Ok() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    public void testThatListBooksReturnsBook() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        testBookEntityA.setIsbn("123456789");
        testBookEntityA.setTitle("Crossword Puzzles");
        bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookEntityA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(testBookEntityA.getTitle())
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsHttp200OkWhenBookExists() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }
    @Test
    public void testThatGetBookSuccessfullyReturnsHttp404OkWhenBookDoesntExist() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/999" )
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }
    @Test
    public void testThatUpdateBookReturnsHttpStatus200Ok() throws Exception{
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        BookEntity savedBookEntity = bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDto(null);
        testBookA.setIsbn(savedBookEntity.getIsbn());
        String bookJson = objectMapper.writeValueAsString(testBookEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatUpdateBookReturnsUpdatedBook()throws Exception{
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        BookEntity savedBookEntity = bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDto(null);
        testBookA.setIsbn(savedBookEntity.getIsbn());
        testBookA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("1617292540")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );

    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook()throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);
        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        BookEntity savedBook=bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDto(null);
        testBookA.setTitle("UPDATED");
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")

        );

    }

    @Test
    public void testThatDeletingExistingBookReturns204NoContent() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);

        BookEntity testBookEntityA = TestDataUtil.createTestBookEntityA(testAuthorB);
        BookEntity savedBookEntity = bookService.createUpdateBook(testBookEntityA.getIsbn(), testBookEntityA);

        BookDto testBookA = TestDataUtil.createTestBookDto(null);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + testBookEntityA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

    }
}