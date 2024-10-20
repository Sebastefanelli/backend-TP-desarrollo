package com.example.practica.models;

import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name="alumno") //para cambiar el nombre de la tabla de la bdd
public class Alumno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="nombre")
	private String nombre;
	@Column(name = "fecha_nacimiento")
	private LocalDate fecha_nacimiento;



	//constructor
	public Alumno(){}

	public Alumno(String nombre, LocalDate fecha_nacimiento){
		this.fecha_nacimiento= fecha_nacimiento;
		this.nombre=nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaNacimiento() {
		return fecha_nacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fecha_nacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}