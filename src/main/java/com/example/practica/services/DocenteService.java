package com.example.practica.services;


import com.example.practica.models.Docente;
import com.example.practica.repositories.IDocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DocenteService {

	@Autowired
	private IDocenteRepository docenteRepository;

	public List<Docente> getAllDocentes() {
		return docenteRepository.findAll();
	}

	public Docente saveDocente(Docente docente) {
		return docenteRepository.save(docente);
	}

	public Optional<Docente> getDocenteById(Long id) {
		return docenteRepository.findById(id);
	}

	public Docente updateDocente(Long id, Docente request) {
		Docente docente = docenteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Docente not found"));

		docente.setNombre(request.getNombre());
		docente.setLegajo(request.getLegajo());
		return docenteRepository.save(docente);
	}

	public boolean deleteDocente(Long id) {
		try {
			docenteRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
