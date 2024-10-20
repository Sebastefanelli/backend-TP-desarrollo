package com.example.practica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.practica.models.Curso;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;


public interface ICursoRepository extends JpaRepository<Curso, Long> {
        List<Curso> findByFechaFin(LocalDate fechaFin);
        Optional<Curso> findByDocenteIdAndFechaFinAfter(Long docenteId, LocalDate currentDate);
}