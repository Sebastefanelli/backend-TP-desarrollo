package com.example.practica.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practica.models.Curso;
import com.example.practica.models.Alumno;
import com.example.practica.models.Docente;
import com.example.practica.models.Tema;

import com.example.practica.repositories.ICursoRepository;
import com.example.practica.repositories.IDocenteRepository;
import com.example.practica.repositories.IAlumnoRepository;
import com.example.practica.repositories.ITemaRepository;


@Service
public class CursoService {

	@Autowired
	ICursoRepository cursoRepository;

	public ArrayList<Curso> getCursos() {
		return (ArrayList<Curso>) cursoRepository.findAll();
	}

	@Autowired
	IDocenteRepository docenteRepository;

	@Autowired
	ITemaRepository temaRepository;

	@Autowired
	IAlumnoRepository alumnoRepository;

	public Curso saveCurso(Curso curso) {
		try {
			// Fetch existing Docente and Tema from the database
			Docente existingDocente = docenteRepository.findById(curso.getDocente().getId())
					.orElseThrow(() -> new RuntimeException("Docente not found"));
			Tema existingTema = temaRepository.findById(curso.getTema().getId())
					.orElseThrow(() -> new RuntimeException("Tema not found"));

			// Replace with the existing entities
			curso.setDocente(existingDocente);
			curso.setTema(existingTema);

			// Fetch existing Alumnos from the database
			List<Alumno> existingAlumnos = new ArrayList<>();
			for (Alumno alumno : curso.getAlumnos()) {
				Alumno existingAlumno = alumnoRepository.findById(alumno.getId())
						.orElseThrow(() -> new RuntimeException("Alumno not found with ID: " + alumno.getId()));
				existingAlumnos.add(existingAlumno);
			}

			// Set the list of existing Alumnos
			curso.setAlumnos(existingAlumnos);

			return cursoRepository.save(curso);
		} catch (Exception e) {
			System.err.println("Error saving curso: " + e.getMessage());
			throw new RuntimeException("Error saving curso: " + e.getMessage());
		}
	}

	public Optional<Curso> getById(Long id) {
		return cursoRepository.findById(id);
	}

	public Curso updateById(Curso request, Long id) {
		Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso not found"));

		// Fetch existing Docente and Tema from the database
		Docente existingDocente = docenteRepository.findById(request.getDocente().getId())
				.orElseThrow(() -> new RuntimeException("Docente not found"));
		Tema existingTema = temaRepository.findById(request.getTema().getId())
				.orElseThrow(() -> new RuntimeException("Tema not found"));

		// Replace with the existing entities
		curso.setDocente(existingDocente);
		curso.setTema(existingTema);

		// Fetch existing Alumnos from the database
		List<Alumno> existingAlumnos = new ArrayList<>();
		for (Alumno alumno : request.getAlumnos()) {
			Alumno existingAlumno = alumnoRepository.findById(alumno.getId())
					.orElseThrow(() -> new RuntimeException("Alumno not found with ID: " + alumno.getId()));
			existingAlumnos.add(existingAlumno);
		}

		// Set the list of existing Alumnos
		curso.setAlumnos(existingAlumnos);

		// Update other fields
		curso.setFechaInicio(request.getFechaInicio());
		curso.setFechaFin(request.getFechaFin());
		curso.setPrecio(request.getPrecio());

		return cursoRepository.save(curso);
	}


	public Boolean deleteCurso(Long id) {
		try {
			cursoRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public List<Curso> getCursosByFechaFin(LocalDate fechaFin) {
		return cursoRepository.findByFechaFin(fechaFin);
	}

	public List<Alumno> getAlumnosByDocenteId(Long docenteId) {
		Optional<Curso> currentCurso = cursoRepository.findByDocenteIdAndFechaFinAfter(docenteId, LocalDate.now());
		return currentCurso.map(Curso::getAlumnos).orElse(new ArrayList<>());
	}
	public List<Alumno> getAlumnosByDocenteLegajo(Long legajo) {
		// Primero, encuentra al docente usando el legajo
		Optional<Docente> docente = docenteRepository.findByLegajo(legajo);

		// Si el docente existe, busca los cursos activos y obtiene los alumnos
		if (docente.isPresent()) {
			Optional<Curso> currentCurso = cursoRepository.findByDocenteIdAndFechaFinAfter(docente.get().getId(), LocalDate.now());
			return currentCurso.map(Curso::getAlumnos).orElse(new ArrayList<>());
		} else {
			return new ArrayList<>(); // Retorna una lista vacía si el docente no se encuentra
		}
	}


}
