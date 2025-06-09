package com.LAB1.spring.boot.API.Producto.Service;

import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> getAllProducto() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProducto(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto updateProducto(Integer id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setCategoria(productoActualizado.getCategoria());
            return productoRepository.save(producto);
        }else{
            throw new RuntimeException("producto no encontrado");
        }
    }

    //borrar categoria
    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }

}
