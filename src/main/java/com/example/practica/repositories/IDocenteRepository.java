package com.example.practica.repositories;

import com.example.practica.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByLegajo(Long legajo);
}
