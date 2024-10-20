package com.example.practica.repositories;

import com.example.practica.models.Tema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITemaRepository extends JpaRepository<Tema, Long> {
}
