package com.projetinho.projetinho_de_recapitulacao.services;

import com.projetinho.projetinho_de_recapitulacao.controllers.PersonController;
import com.projetinho.projetinho_de_recapitulacao.dto.v1.PersonDTO;
import com.projetinho.projetinho_de_recapitulacao.dto.v2.PersonDTOV2;
import com.projetinho.projetinho_de_recapitulacao.exception.ResourceNotFoundException;
import com.projetinho.projetinho_de_recapitulacao.mapper.custom.PersonMapper;
import com.projetinho.projetinho_de_recapitulacao.model.Person;
import com.projetinho.projetinho_de_recapitulacao.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static com.projetinho.projetinho_de_recapitulacao.mapper.ObjectMapper.parseListObject;
import static com.projetinho.projetinho_de_recapitulacao.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    PersonRepository repository;
    @Autowired
    PersonMapper converter;
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    public PersonServices(PersonRepository repository) {
        this.repository = repository;
    }

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<PersonDTO> findAll() {
        logger.info("Finding all people!");
        var persons = parseListObject(repository.findAll(),  PersonDTO.class);
        persons.forEach(this::addHateoasLinks);
        return persons;
    }

    public PersonDTO findById(Long id) {
        logger.info("Find one person!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
        var dto = parseObject( entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public PersonDTO create(PersonDTO person){
        logger.info("Create new person!");
        var entity = parseObject(person, Person.class);
        var dto =parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public PersonDTOV2 createV2(PersonDTOV2 person){
        logger.info("Create new person V2!");
        var entity = converter.convertDTOToEntity(person);
        return converter.convertEntityToDTO(repository.save(entity));
    }
    public PersonDTO update(PersonDTO person){
        logger.info("Update person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public void delete(Long id){
        logger.info("Delete person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
        repository.delete(entity);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
