package com.LAB1.spring.boot.API.Venta.Controller;

import com.LAB1.spring.boot.API.Inventario.Service.InventarioService;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import com.LAB1.spring.boot.API.Venta.DTO.VentaDTO;
import com.LAB1.spring.boot.API.Venta.Model.Venta;
import com.LAB1.spring.boot.API.Venta.Repository.VentaRepository;
import com.LAB1.spring.boot.API.Venta.Service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/LAB")
public class VentaController {
    private final VentaService ventaService;
    private final InventarioService inventarioService;


    @Autowired
    public VentaController(VentaService ventaService,InventarioService inventarioService) {
        this.ventaService = ventaService;
        this.inventarioService = inventarioService;
    }

    @PostMapping("/venta")
    public ResponseEntity<?> addVenta(@RequestBody VentaDTO ventaDTO) {
        try{
            boolean sufInventario = inventarioService.revisarSufInvent(ventaDTO.getIdProducto(), ventaDTO.getCantidad());
            if(sufInventario){
                //si hay suficiente inventario
                ventaService.saveVenta(ventaDTO);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Venta Guardada");
            }else{
                //si no hay suficiente inventario
                return ResponseEntity.status(HttpStatus.CONFLICT).body("No hay suficiente inventario para el articulo id: " + ventaDTO.getIdProducto());
            }
        }catch(Exception e){
            System.err.println("Error al crear la venta: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear venta");
        }
    }

    @GetMapping("/venta")
    public ResponseEntity<?> getAllVentas() {
        try{
            List<VentaDTO> ventas = ventaService.getAllVentas();
            System.out.println(ventas);
            return ResponseEntity.ok(ventas);
        }catch(Exception e){
            System.err.println("Error al obtener las ventas: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener ventas");
        }
    }

    @GetMapping("/venta/{id}")
    public ResponseEntity<?> getVentaById(@PathVariable int id) {
        try{
            Optional<VentaDTO> venta = ventaService.getVenta(id);
            return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Exception e){
            System.err.println("Error al obtener la venta: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener la venta");
        }
    }

    //get ventas por factura especifica
    @GetMapping("/venta/producto/{idFactura}")
    public ResponseEntity<?> getVentabyFacturaId(@PathVariable int idFactura) {
        try{
            List<VentaDTO> ventas = ventaService.getVentaPorFactura(idFactura);
            return ResponseEntity.ok(ventas);
        }catch(Exception e){
            System.err.println("Error al obtener las ventas: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener las ventas");
        }
    }

    @PutMapping("/venta/{id}")
    public ResponseEntity<?> updateVenta(@PathVariable int id,@RequestBody VentaDTO ventaDTO) {
        try{
            VentaDTO updatedVenta = ventaService.updateVenta(id,ventaDTO);
            return ResponseEntity.ok(updatedVenta);
        }catch(Exception e){
            System.err.println("Error al actualizar La venta: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto");
        }
    }

    @DeleteMapping("/venta/{id}")
    public ResponseEntity<?> deleteVenta(@PathVariable int id) {
        try{
            ventaService.deleteVenta(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Venta eliminada con exito");
        }catch(Exception e){
            System.err.println("Error al eliminar la venta: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la venta");
        }
    }



}
