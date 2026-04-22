package com.projetinho.projetinho_de_recapitulacao.repository;

import com.projetinho.projetinho_de_recapitulacao.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
