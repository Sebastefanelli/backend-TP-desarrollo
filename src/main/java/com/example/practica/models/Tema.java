package com.example.practica.models;

import jakarta.persistence.*;



@Entity
@Table(name="tema") //para cambiar el nombre de la tabla de la bdd
public class Tema {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="nombre")
	private String nombre;
	@Column(name = "descripcion")
	private String descripcion;

	//constructor
	public Tema(){}

	public Tema(String nombre, String descripcion){
		this.descripcion = descripcion;
		this.nombre=nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}