package com.LAB1.spring.boot.API.Factura.DTO;
import java.util.Date;

public class FacturaDTO {
    private int id;
    private double monto;
    private Date fecha;

    public FacturaDTO() {
    }

    public FacturaDTO(int id, double monto, Date fecha) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
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
        return "FacturaDTO{" +
                "id=" + id +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }
}
