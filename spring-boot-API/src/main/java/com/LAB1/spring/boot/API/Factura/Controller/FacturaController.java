package com.LAB1.spring.boot.API.Factura.Controller;

import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Factura.DTO.FacturaDTO;
import com.LAB1.spring.boot.API.Factura.Service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/LAB")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacturaController {
    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping("/factura")
    public ResponseEntity<?> addFactura(@RequestBody FacturaDTO facturaDTO) {
        try{
            FacturaDTO newfacturaDTO = facturaService.saveFactura(facturaDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Factura creada con exito");
        }catch(Exception e){
            System.err.println("Error al crear la factura: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear factura");
        }
    }

    //Listar todas las categorias
    @GetMapping("/factura")
    public ResponseEntity<?> getAllFacturas() {
        try{
            List<FacturaDTO> facturas = facturaService.getAllFacturas();
            return ResponseEntity.ok(facturas);
        }catch(Exception e){
            System.err.println("Error al obtener las facturas: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener facturas");
        }
    }

    @GetMapping("/factura/{id}")
    public ResponseEntity<?> getFacturaById(@PathVariable int id) {
        try{
            Optional<FacturaDTO> factura = facturaService.getFactura(id);
            return factura.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Exception e){
            System.err.println("Error al obtener la factura: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener la factura");
        }
    }

    @PutMapping("/factura/{id}")
    public ResponseEntity<?> updateFactura(@PathVariable int id,@RequestBody FacturaDTO facturaDTO) {
        try{
            FacturaDTO updatedFactura = facturaService.updateFactura(id, facturaDTO);
            return ResponseEntity.ok(updatedFactura);
        }catch(Exception e){
            System.err.println("Error al actualizar la factura: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la factura");
        }
    }

    @DeleteMapping("/factura/{id}")
    public ResponseEntity<?> deleteFactura(@PathVariable int id) {
        try{
            facturaService.deleteFactura(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Factura eliminada con exito");
        }catch(Exception e){
            System.err.println("Error al eliminar la factura: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la factura");
        }
    }

}
