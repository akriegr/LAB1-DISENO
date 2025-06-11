package com.LAB1.spring.boot.API.Factura.Model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "AK_Factura", schema = "u484426513_dsc225")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false) private Double monto;
    @Column(nullable = false) private Date fecha;
    public Factura() {}
    public Factura(Double monto, Date fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }
}
