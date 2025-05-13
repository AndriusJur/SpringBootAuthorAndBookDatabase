package com.andrius.database.repositories;

import com.andrius.database.TestDataUtil;
import com.andrius.database.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTests {

   @Autowired  // single constructor, but tests require autowire
   private AuthorRepository repository;

   @Test
   public void  testThatAuthorCanBeCreatedAndRecalled(){
      Author author= TestDataUtil.createTestAuthorA();
      repository.save(author); //inbuilt save - in spring data JPA
      Optional<Author>result= repository.findById(author.getId());//findById - JPA inbuilt findOne
      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(author);


   }
   @Test
   public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
      Author authorA= TestDataUtil.createTestAuthorA();
      repository.save(authorA);
      Iterable<Author> result= repository.findAll(); //creates iterable - not list!

      Author authorB=TestDataUtil.createTestAuthorB();
      repository.save(authorB);
      result= repository.findAll(); //creates iterable - not list!

      Author authorC=TestDataUtil.createTestAuthorC();
      repository.save(authorC);

       result= repository.findAll(); //creates iterable - not list!
      assertThat(result)
              .hasSize(3)
              .containsExactly(authorA,authorB,authorC);
   }

   @Test
   public void testThatAuthorCanBeUpdated(){
      Author authorA= TestDataUtil.createTestAuthorA();
      repository.save(authorA);
      authorA.setName("UPDATED");
      repository.save(authorA);//save is used for creating AND updating
      Optional<Author> result = repository.findById(authorA.getId());
      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(authorA);

   }
   @Test
   public void testThatAuthorCanBeDeleted(){
      Author authorA= TestDataUtil.createTestAuthorA();
      repository.save(authorA);
      repository.deleteById(authorA.getId());
      Optional<Author> result = repository.findById(authorA.getId());
      assertThat(result).isEmpty();

   }
   @Test
   public void testThatGetAuthorsWithAgeLessThan(){
      Author testAuthorA = TestDataUtil.createTestAuthorA();
      repository.save(testAuthorA);
      Author testAuthorB = TestDataUtil.createTestAuthorB();
      repository.save(testAuthorB);
      Author testAuthorC = TestDataUtil.createTestAuthorC();
      repository.save(testAuthorC);

      Iterable <Author> result=repository.ageLessThan(50); //JPA works out method based on the name of method.
      assertThat(result)
              .containsExactly(testAuthorB,testAuthorC);
   }

   @Test
   public void testThatGetAuthorsWithAgeGreaterThan(){
      Author testAuthorA = TestDataUtil.createTestAuthorA();
      repository.save(testAuthorA);
      Author testAuthorB = TestDataUtil.createTestAuthorB();
      repository.save(testAuthorB);
      Author testAuthorC = TestDataUtil.createTestAuthorC();
      repository.save(testAuthorC);

      Iterable <Author> result= repository.findAuthorsWithAgeGreaterThan(50);
      assertThat(result).containsExactly(testAuthorA);
   }
}
