package com.LAB1.spring.boot.API.Inventario.Controller;

import com.LAB1.spring.boot.API.Inventario.DTO.InventarioDTO;
import com.LAB1.spring.boot.API.Inventario.Service.InventarioService;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/LAB")
public class InventarioController {

    private final InventarioService inventarioService;

    @Autowired
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/inventario")
    public ResponseEntity<?> addInventario(@RequestBody InventarioDTO inventarioDTO) {
        try{
            InventarioDTO nuevoInventario = inventarioService.saveInventario(inventarioDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Inventario creado con exito");
        }catch(Exception e){
            System.err.println("Error al crear el inventario: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear inventario");
        }
    }

    @GetMapping("/inventario")
    public ResponseEntity<?> getAllInventario() {
        try{
            List<InventarioDTO> inventario = inventarioService.getAllInventario();
            System.out.println(inventario);
            return ResponseEntity.ok(inventario);
        }catch(Exception e){
            System.err.println("Error al obtener el inventario: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el inventario");
        }
    }

    //get inventario de un producto especifico
    @GetMapping("/inventario/{id}")
    public ResponseEntity<?> getInventariobyProductoId(@PathVariable int id) {
        try{
            Optional<InventarioDTO> inventario = inventarioService.getInventarioByIdProducto(id);
            return inventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Exception e){
            System.err.println("Error al obtener el inventario del producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener el inventario del producto");
        }
    }

    @PutMapping("/inventario/{id}")
    public ResponseEntity<?> updateInventario(@PathVariable int id,@RequestBody InventarioDTO inventarioDTO) {
        try{
            InventarioDTO updatedInventario = inventarioService.updateInventario(id, inventarioDTO);
            return ResponseEntity.ok(updatedInventario);
        }catch(Exception e){
            System.err.println("Error al actualizar el inventario: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el inventario");
        }
    }

    @DeleteMapping("/inventario/{id}")
    public ResponseEntity<?> deleteInventario(@PathVariable int id) {
        try{
            inventarioService.deleteInventario(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Inventario eliminado con exito");
        }catch(Exception e){
            System.err.println("Error al eliminar el producto: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el inventario");
        }
    }

}
