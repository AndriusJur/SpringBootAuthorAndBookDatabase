package controllers;

import com.andrius.database.BooksApiApplication;
import com.andrius.database.TestDataUtil;
import com.andrius.database.domain.dto.AuthorDto;
import com.andrius.database.domain.entities.AuthorEntity;
import com.andrius.database.services.AuthorService;
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

public class AuthorControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorService authorService;


    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorB);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorB);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Vardenis Pavardenis")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("44")

        );
    }
    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        authorService.save(testAuthorB);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()//0 - first in db, * - any in db

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(testAuthorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(testAuthorB.getAge())

        );;
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        authorService.save(testAuthorB);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + testAuthorB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        authorService.save(testAuthorB);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthorB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorB.getAge())

        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity testAuthorA1 = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(testAuthorA1);

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity testAuthorA1 = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(testAuthorA1);

        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(savedAuthor.getId());

        String testAuthorBtoJson = objectMapper.writeValueAsString(testAuthorB);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAuthorBtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        AuthorEntity testAuthorA1 = TestDataUtil.createTestAuthorA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorA1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200Ok() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorDto testAuthorA = TestDataUtil.createTestAuthorDtoA();
        testAuthorA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);

        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        testAuthorDtoA.setName("UPDATED");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDtoA.getAge())

        );
    }
    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(testAuthorEntityA);



        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }




}
