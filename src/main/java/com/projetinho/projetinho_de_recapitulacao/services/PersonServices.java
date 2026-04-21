package com.projetinho.projetinho_de_recapitulacao.services;

import com.projetinho.projetinho_de_recapitulacao.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll() {
        logger.info("Finding all people!");
        List<Person>  persons = new ArrayList<Person>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person findById(String id){
        logger.info("Find one person!");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Luan");
        person.setLastName("Abreu");
        person.setAddress("Criciuma -SC");
        person.setGender("Male");
        return person;
    }
    public Person create(Person person){
        logger.info("Create new person!");
        return person;
    }
    public Person update(Person person){
        logger.info("Update person!");
        return person;
    }
    public void delete(String id){
        logger.info("Delete person!");
    }

    private Person mockPerson(int i){
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Firstname " + i);
        person.setLastName("Lastname " + i);
        person.setAddress("Some place in Brazil");
        person.setGender("Male");
        return person;
    }
}
