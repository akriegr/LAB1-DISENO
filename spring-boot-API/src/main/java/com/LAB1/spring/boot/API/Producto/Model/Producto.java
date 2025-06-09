package com.LAB1.spring.boot.API.Producto.Model;
import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import jakarta.persistence.*;

@Entity
@Table(name="AK_Producto", schema = "u484426513_dsc225")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name="nombre",nullable = false) private String nombre;
    @Column(name="precio",nullable = false) private Double precio;
    @ManyToOne
    @JoinColumn (name = "idCategoria",nullable = false) private Categoria categoria;

    public Producto() {}

    public Producto(Integer id, String nombre, Double precio, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria=" + categoria +
                '}';
    }
}
