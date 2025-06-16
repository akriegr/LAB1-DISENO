package com.LAB1.spring.boot.API.Producto.Controller;

import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/LAB")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/producto")
    public ResponseEntity<?> addProducto(@RequestBody ProductoDTO productoDTO) {
        try{
            productoService.saveProducto(productoDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Producto creado con exito");
        }catch(Exception e){
            System.err.println("Error al crear el producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear producto");
        }
    }

    @GetMapping("/producto")
    public ResponseEntity<?> getAllProductos() {
        try{
            List<ProductoDTO> productos = productoService.getAllProductos();
            System.out.println(productos);
            return ResponseEntity.ok(productos);
        }catch(Exception e){
            System.err.println("Error al obtener las productos: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener productos");
        }
    }

    //Obtener prodcuto especifica
    @GetMapping("/producto/{id}")
    public ResponseEntity<?> getProductoById(@PathVariable int id) {
        try{
            Optional<ProductoDTO> producto = productoService.getProducto(id);
            return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Exception e){
            System.err.println("Error al obtener la categoria: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener el producto");
        }
    }

    @PutMapping("/producto/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable int id,@RequestBody ProductoDTO productoDTO) {
        try{
            ProductoDTO updatedProducto = productoService.updateProducto(id, productoDTO);
            return ResponseEntity.ok(updatedProducto);
        }catch(Exception e){
            System.err.println("Error al actualizar el producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto");
        }
    }

    @DeleteMapping("/producto/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable int id) {
        try{
            productoService.deleteProducto(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Producto eliminado con exito");
        }catch(Exception e){
            System.err.println("Error al eliminar el producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto");
        }
    }
}

