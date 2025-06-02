package com.andrius.database.repositories;

import com.andrius.database.TestDataUtil;
import com.andrius.database.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {

   @Autowired  // single constructor, but tests require autowire
   private AuthorRepository repository;

   @Test
   public void  testThatAuthorCanBeCreatedAndRecalled(){
      AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
      repository.save(authorEntity); //inbuilt save - in spring data JPA
      Optional<AuthorEntity>result= repository.findById(authorEntity.getId());//findById - JPA inbuilt findOne
      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(authorEntity);


   }
   @Test
   public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
      AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
      repository.save(authorEntityA);
      Iterable<AuthorEntity> result= repository.findAll(); //creates iterable - not list!

      AuthorEntity authorEntityB =TestDataUtil.createTestAuthorB();
      repository.save(authorEntityB);
      result= repository.findAll(); //creates iterable - not list!

      AuthorEntity authorEntityC =TestDataUtil.createTestAuthorC();
      repository.save(authorEntityC);

       result= repository.findAll(); //creates iterable - not list!
      assertThat(result)
              .hasSize(3)
              .containsExactly(authorEntityA, authorEntityB, authorEntityC);
   }

   @Test
   public void testThatAuthorCanBeUpdated(){
      AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
      repository.save(authorEntityA);
      authorEntityA.setName("UPDATED");
      repository.save(authorEntityA);//save is used for creating AND updating
      Optional<AuthorEntity> result = repository.findById(authorEntityA.getId());
      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(authorEntityA);

   }
   @Test
   public void testThatAuthorCanBeDeleted(){
      AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
      repository.save(authorEntityA);
      repository.deleteById(authorEntityA.getId());
      Optional<AuthorEntity> result = repository.findById(authorEntityA.getId());
      assertThat(result).isEmpty();

   }
//   @Test
//   public void testThatGetAuthorsWithAgeLessThan(){
//      AuthorEntity testAuthorDtoA = TestDataUtil.createTestAuthorA();
//      repository.save(testAuthorDtoA);
//      AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorB();
//      repository.save(testAuthorEntityB);
//      AuthorEntity testAuthorEntityC = TestDataUtil.createTestAuthorC();
//      repository.save(testAuthorEntityC);
//
//      Iterable <AuthorEntity> result=repository.ageLessThan(50); //JPA works out method based on the name of method.
//      assertThat(result)
//              .containsExactly(testAuthorEntityB, testAuthorEntityC);
//   }
//
//   @Test
//   public void testThatGetAuthorsWithAgeGreaterThan(){
//      AuthorEntity testAuthorDtoA = TestDataUtil.createTestAuthorA();
//      repository.save(testAuthorDtoA);
//      AuthorEntity testAuthorEntityB = TestDataUtil.createTestAuthorB();
//      repository.save(testAuthorEntityB);
//      AuthorEntity testAuthorEntityC = TestDataUtil.createTestAuthorC();
//      repository.save(testAuthorEntityC);
//
//      Iterable <AuthorEntity> result= repository.findAuthorsWithAgeGreaterThan(50);
//      assertThat(result).containsExactly(testAuthorDtoA);
//   }
}
