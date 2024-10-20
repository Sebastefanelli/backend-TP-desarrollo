package com.example.practica.services;

import com.example.practica.models.Alumno;
import com.example.practica.repositories.IAlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

	@Autowired
	private IAlumnoRepository alumnoRepository;

	public List<Alumno> getAllAlumnos() {
		return alumnoRepository.findAll();
	}

	public Alumno saveAlumno(Alumno alumno) {
		return alumnoRepository.save(alumno);
	}

	public Optional<Alumno> getAlumnoById(Long id) {
		return alumnoRepository.findById(id);
	}

	public Alumno updateAlumno(Long id, Alumno request) {
		Alumno alumno = alumnoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Alumno not found"));

		alumno.setNombre(request.getNombre());
		alumno.setFechaNacimiento(request.getFechaNacimiento());
		// Añade más campos si es necesario
		return alumnoRepository.save(alumno);
	}

	public boolean deleteAlumno(Long id) {
		try {
			alumnoRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
