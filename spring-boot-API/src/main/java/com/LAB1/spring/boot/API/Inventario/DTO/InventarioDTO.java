package com.LAB1.spring.boot.API.Inventario.DTO;

public class InventarioDTO {
    private int id;
    private int cantidad;
    private int idProducto;

    public InventarioDTO() {
    }
    public InventarioDTO(int id, int cantidad, int idProducto) {
        this.id = id;
        this.cantidad = cantidad;
        this.idProducto = idProducto;

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

    @Override
    public String toString() {
        return "InventarioDTO{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", idProducto=" + idProducto +
                '}';
    }
}
