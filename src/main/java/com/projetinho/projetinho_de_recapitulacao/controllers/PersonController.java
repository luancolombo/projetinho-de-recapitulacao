package com.projetinho.projetinho_de_recapitulacao.controllers;

import com.projetinho.projetinho_de_recapitulacao.controllers.docs.PersonControllerDocs;
import com.projetinho.projetinho_de_recapitulacao.dto.v1.PersonDTO;
import com.projetinho.projetinho_de_recapitulacao.dto.v2.PersonDTOV2;
import com.projetinho.projetinho_de_recapitulacao.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@Tag(name = ("People"), description = ("Endpoints for Managing People"))
public class PersonController implements PersonControllerDocs {

    @Autowired
    private final PersonServices service;

    public PersonController(PersonServices service) {
        this.service = service;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public List<PersonDTO> findAll(){
        return service.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO findById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PostMapping(value = "/v2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTOV2 create(@RequestBody PersonDTOV2 person){

        return service.createV2(person);
    }
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO create(@Valid @RequestBody PersonDTO person){
        return service.create(person);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO update(@Valid @RequestBody PersonDTO person){
        return service.update(person);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.delete(id);

    return ResponseEntity.noContent().build();
    }
}
