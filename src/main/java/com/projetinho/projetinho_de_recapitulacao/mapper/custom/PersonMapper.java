package com.projetinho.projetinho_de_recapitulacao.mapper.custom;

import com.projetinho.projetinho_de_recapitulacao.dto.v2.PersonDTOV2;
import com.projetinho.projetinho_de_recapitulacao.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person){
        PersonDTOV2 personDTO = new PersonDTOV2();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setAddress(person.getAddress());
        personDTO.setGender(person.getGender());
        personDTO.setBirthDate(new Date());
        return personDTO;
    }
    public Person convertDTOToEntity(PersonDTOV2 person){
        Person entity = new Person();
        entity.setId(person.getId());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        // entity.setBirthDate(new Date());
        return entity;
    }
}
