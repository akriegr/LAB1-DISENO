package com.LAB1.spring.boot.API.Categoria.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "AK_Categoria", schema = "u484426513_dsc225")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column (name="nombre",nullable = false)private String nombre;
    @Column (name="descripcion",nullable = false)private String descripcion;
    public Categoria() {

    }
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
