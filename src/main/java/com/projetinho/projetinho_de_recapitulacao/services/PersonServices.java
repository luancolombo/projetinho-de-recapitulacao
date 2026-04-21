package com.projetinho.projetinho_de_recapitulacao.services;

import com.projetinho.projetinho_de_recapitulacao.model.Person;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

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
}
