package com.example.practica.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

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
			// Fetch Docente y Tema de la base de datos
			Docente docenteExistente = docenteRepository.findById(curso.getDocente().getId())
					.orElseThrow(() -> new RuntimeException("Docente not encontrado"));
			Tema temaExistente = temaRepository.findById(curso.getTema().getId())
					.orElseThrow(() -> new RuntimeException("Tema no encontrado"));

			// remplazo por las entidades existentes
			curso.setDocente(docenteExistente);
			curso.setTema(temaExistente);

			// Fetch Alumnos existentes en la base de datos
			List<Alumno> alumnosExistentes = new ArrayList<>();
			for (Alumno alumno : curso.getAlumnos()) {
				Alumno alumnoExistente = alumnoRepository.findById(alumno.getId())
						.orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumno.getId()));
				alumnosExistentes.add(alumnoExistente);
			}


			// Seteo la lista con Alumnos existentes
			curso.setAlumnos(alumnosExistentes);

			return cursoRepository.save(curso);
		} catch (Exception e) {
			System.err.println("Error guradando el curso: " + e.getMessage());
			throw new RuntimeException("Error guardando el curso: " + e.getMessage());
		}
	}

	public Optional<Curso> getById(Long id) {
		return cursoRepository.findById(id);
	}

	public Curso updateById(Curso request, Long id) {
		Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso not found"));

		// Fetch Docente y Tema de la base de datos
		Docente docenteExistente = docenteRepository.findById(request.getDocente().getId())
				.orElseThrow(() -> new RuntimeException("Docente no encontrado"));
		Tema temaExistente = temaRepository.findById(request.getTema().getId())
				.orElseThrow(() -> new RuntimeException("Tema no encontrado"));

		// remplazo por las entidades existentes
		curso.setDocente(docenteExistente);
		curso.setTema(temaExistente);

		// Fetch Alumnos existentes en la base de datos
		List<Alumno> alumnosExistentes = new ArrayList<>();
		for (Alumno alumno : request.getAlumnos()) {
			Alumno alumnoExistente = alumnoRepository.findById(alumno.getId())
					.orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumno.getId()));
			alumnosExistentes.add(alumnoExistente);
		}

		// Seteo la lista con Alumnos existentes
		curso.setAlumnos(alumnosExistentes);

		// Update de los campos
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
