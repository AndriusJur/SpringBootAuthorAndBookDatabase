package com.andrius.database.controllers;

import com.andrius.database.domain.entities.AuthorEntity;
import com.andrius.database.domain.dto.AuthorDto;
import com.andrius.database.mappers.Mapper;
import com.andrius.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// ////////
/// // this is  presentation layer
/// ////
@RestController  //since we are creating REST API
public class AuthorController {

    private AuthorService authorService;

    private Mapper<AuthorEntity,AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity,AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper=authorMapper;
    }

    @PostMapping(path = "/authors") //POST endpoint - creates new author
    public ResponseEntity <AuthorDto> createAuthor(@RequestBody AuthorDto author){ //look at http request body for author object ; ResponseEntity - control over http status, response headers, body
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }
    @GetMapping(path="/authors") //READ MANY -> get
    public List<AuthorDto>listAuthors(){
        List <AuthorEntity>authors= authorService.findAll();
        return authors.stream()
                .map(authorMapper::mapTo).collect(Collectors.toList());
    }
    @GetMapping(path = "/authors/{id}")//READ ONE endpoint
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id")Long id){
        Optional <AuthorEntity> foundAuthor= authorService.findOne(id);//if not found - optional empty
        return foundAuthor.map(authorEntity-> {
            AuthorDto authorDto=authorMapper.mapTo(authorEntity);
            return new ResponseEntity(authorDto, HttpStatus.OK);

        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {

        if (!authorService.isExists(id)) { //isExists has to be implemented
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(
                authorMapper.mapTo(savedAuthorEntity),
                HttpStatus.OK);
    }
    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto
    ){
        if (!authorService.isExists(id)) { //isExists has to be implemented
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity updatedAuthor = authorService.partialUpdate(id,authorEntity);
        return new ResponseEntity<>(
                authorMapper.mapTo(updatedAuthor),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor (@PathVariable("id") Long id){
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);//204
    }

    @DeleteMapping(path = "/authors")    //nuclear option
    public ResponseEntity deleteAllAuthors(){
        authorService.delete();
        return new ResponseEntity(HttpStatus.NO_CONTENT);//204
    }

}
