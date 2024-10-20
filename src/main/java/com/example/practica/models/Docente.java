package com.example.practica.models;

import jakarta.persistence.*;




@Entity
@Table(name="docente") //para cambiar el nombre de la tabla de la bdd
public class Docente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="nombre")
	private String nombre;
	@Column(name = "legajo")
	private Long legajo;

	//constructor
	public Docente(){}

	public Docente(String nombre, Long legajo){
		this.legajo = legajo;
		this.nombre=nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLegajo() {return legajo;}

	public void setLegajo(Long legajo) {this.legajo = legajo;}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}