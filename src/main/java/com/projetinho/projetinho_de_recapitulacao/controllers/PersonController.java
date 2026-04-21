package com.projetinho.projetinho_de_recapitulacao.controllers;

import com.projetinho.projetinho_de_recapitulacao.model.Person;
import com.projetinho.projetinho_de_recapitulacao.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private final PersonServices service;

    public PersonController(PersonServices service) {
        this.service = service;
    }


    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Person findById(@PathVariable("id") String id){
        return service.findById(id);
    }
}
