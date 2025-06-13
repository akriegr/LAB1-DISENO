package com.LAB1.spring.boot.API.Venta.Model;

import com.LAB1.spring.boot.API.Factura.Model.Factura;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import jakarta.persistence.*;

@Entity
@Table(name = "AK_Venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name="cantidad",nullable=false) private Integer cantidad;
    @ManyToOne
    @JoinColumn(name="idProducto",nullable=false) private Producto producto;
    @ManyToOne
    @JoinColumn(name="idFactura",nullable=false) private Factura factura;

    public Venta (){}

    public Venta (Integer id,Integer cantidad,Producto producto,Factura factura){
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
        this.factura = factura;
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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", producto=" + producto +
                ", factura=" + factura +
                '}';
    }
}
