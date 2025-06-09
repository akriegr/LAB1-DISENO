package com.LAB1.spring.boot.API.Producto.Controller;

import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/LAB")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/producto")
    public ResponseEntity<?> addProducto(@RequestBody Producto producto) {
        try{
            Producto newProducto = productoService.saveProducto(producto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Producto creado con exito");
        }catch(Exception e){
            System.err.println("Error al crear el producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear producto");
        }
    }

}

