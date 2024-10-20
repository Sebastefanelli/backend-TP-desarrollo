package com.example.practica.controllers;

import com.example.practica.models.Docente;
import com.example.practica.services.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

	@Autowired
	private DocenteService docenteService;

	@GetMapping
	public List<Docente> getAllDocentes() {
		return docenteService.getAllDocentes();
	}

	@PostMapping
	public ResponseEntity<Docente> saveDocente(@RequestBody Docente docente) {
		Docente savedDocente = docenteService.saveDocente(docente);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedDocente);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Docente> getDocenteById(@PathVariable Long id) {
		return docenteService.getDocenteById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Docente> updateDocente(@PathVariable Long id, @RequestBody Docente request) {
		Docente updatedDocente = docenteService.updateDocente(id, request);
		return ResponseEntity.ok(updatedDocente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDocente(@PathVariable Long id) {
		if (docenteService.deleteDocente(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
