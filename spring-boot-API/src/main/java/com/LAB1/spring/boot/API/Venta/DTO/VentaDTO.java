package com.LAB1.spring.boot.API.Venta.DTO;

public class VentaDTO {
    private int id;
    private int cantidad;
    private int idProducto;
    private int idFactura;

    public VentaDTO (){}
    public VentaDTO(int id, int cantidad, int idProducto, int idFactura) {
        this.id = id;
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.idFactura = idFactura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    @Override
    public String toString() {
        return "VentaDTO{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", idProducto=" + idProducto +
                ", idFactura=" + idFactura +
                '}';
    }
}
