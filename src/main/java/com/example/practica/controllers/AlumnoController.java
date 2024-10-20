package com.example.practica.controllers;

import com.example.practica.models.Alumno;
import com.example.practica.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;

	@GetMapping
	public List<Alumno> getAllAlumnos() {
		return alumnoService.getAllAlumnos();
	}

	@PostMapping
	public ResponseEntity<Alumno> saveAlumno(@RequestBody Alumno alumno) {
		try {
			Alumno savedAlumno = alumnoService.saveAlumno(alumno);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAlumno);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Alumno>> getAlumnoById(@PathVariable("id") Long id) {
		Optional<Alumno> alumno = alumnoService.getAlumnoById(id);
		return alumno.isPresent() ? ResponseEntity.ok(alumno) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Alumno> updateAlumno(@PathVariable("id") Long id, @RequestBody Alumno request) {
		try {
			Alumno updatedAlumno = alumnoService.updateAlumno(id, request);
			return ResponseEntity.ok(updatedAlumno);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAlumno(@PathVariable("id") Long id) {
		if (alumnoService.deleteAlumno(id)) {
			return ResponseEntity.ok("Alumno with ID " + id + " deleted");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error deleting alumno");
		}
	}
}
