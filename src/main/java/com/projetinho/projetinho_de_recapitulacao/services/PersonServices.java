package com.projetinho.projetinho_de_recapitulacao.services;

import com.projetinho.projetinho_de_recapitulacao.exception.ResourceNotFoundException;
import com.projetinho.projetinho_de_recapitulacao.model.Person;
import com.projetinho.projetinho_de_recapitulacao.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    PersonRepository repository;
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    public PersonServices(PersonRepository repository) {
        this.repository = repository;
    }

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll() {
        logger.info("Finding all people!");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Find one person!");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
    }
    public Person create(Person person){
        logger.info("Create new person!");
        return repository.save(person);
    }
    public Person update(Person person){
        logger.info("Update person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }
    public void delete(Long id){
        logger.info("Delete person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
        repository.delete(entity);
    }
}
