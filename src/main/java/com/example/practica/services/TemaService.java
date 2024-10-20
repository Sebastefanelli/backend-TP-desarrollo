package com.example.practica.services;

import com.example.practica.models.Tema;
import com.example.practica.repositories.ITemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemaService {

	@Autowired
	private ITemaRepository temaRepository;

	public List<Tema> getAllTemas() {
		return temaRepository.findAll();
	}

	public Tema saveTema(Tema tema) {
		return temaRepository.save(tema);
	}

	public Optional<Tema> getTemaById(Long id) {
		return temaRepository.findById(id);
	}

	public Tema updateTema(Long id, Tema request) {
		Tema tema = temaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tema not found"));

		tema.setNombre(request.getNombre());
		tema.setDescripcion(request.getDescripcion());
		// Añade más campos si es necesario
		return temaRepository.save(tema);
	}

	public boolean deleteTema(Long id) {
		try {
			temaRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
