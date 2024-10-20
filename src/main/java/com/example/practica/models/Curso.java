package com.example.practica.models;

import jakarta.persistence.*;

import java.time.LocalDate;

import java.util.List;

@Entity
@Table(name="Curso") //para cambiar el nombre de la tabla de la bdd
public class Curso {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "tema_id")
	private Tema tema;

	private LocalDate fechaInicio;
	private LocalDate fechaFin;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "docente_id")
	private Docente docente;

	private double precio;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "curso_alumno",
			joinColumns = @JoinColumn(name = "curso_id"),
			inverseJoinColumns = @JoinColumn(name = "alumno_id")
	)
	private List<Alumno> alumnos;


	//constructor
	public Curso(){}

	public Curso(Tema tema, LocalDate fechaInicio, LocalDate fechaFin, Docente docente, double precio, List<Alumno> alumnos) {
		this.tema = tema;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.docente = docente;
		this.precio = precio;
		this.alumnos = alumnos;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public void setId(Long id) {
		this.id = id;
	}
}