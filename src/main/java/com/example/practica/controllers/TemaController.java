package com.example.practica.controllers;

import com.example.practica.models.Tema;
import com.example.practica.services.TemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/temas")
public class TemaController {

	@Autowired
	private TemaService temaService;

	@GetMapping
	public List<Tema> getAllTemas() {
		return temaService.getAllTemas();
	}

	@PostMapping
	public ResponseEntity<Tema> saveTema(@RequestBody Tema tema) {
		try {
			Tema savedTema = temaService.saveTema(tema);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedTema);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Tema>> getTemaById(@PathVariable("id") Long id) {
		Optional<Tema> tema = temaService.getTemaById(id);
		return tema.isPresent() ? ResponseEntity.ok(tema) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tema> updateTema(@PathVariable("id") Long id, @RequestBody Tema request) {
		try {
			Tema updatedTema = temaService.updateTema(id, request);
			return ResponseEntity.ok(updatedTema);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTema(@PathVariable("id") Long id) {
		if (temaService.deleteTema(id)) {
			return ResponseEntity.ok("Tema with ID " + id + " deleted");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error deleting tema");
		}
	}
}
