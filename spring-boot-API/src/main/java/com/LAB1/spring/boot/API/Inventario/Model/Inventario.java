package com.LAB1.spring.boot.API.Inventario.Model;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import jakarta.persistence.*;

@Entity
@Table (name = "AK_Inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name="cantidad",nullable = false)private Integer cantidad;
    @OneToOne
    @JoinColumn(name ="idProducto",nullable = false) private Producto producto;
    public Inventario() {}

    public Inventario(Integer id, Integer cantidad, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", producto=" + producto +
                '}';
    }
}
