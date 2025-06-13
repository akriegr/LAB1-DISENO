package com.LAB1.spring.boot.API.Venta.Service;

import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Factura.Model.Factura;
import com.LAB1.spring.boot.API.Factura.Service.FacturaService;
import com.LAB1.spring.boot.API.Inventario.Model.Inventario;
import com.LAB1.spring.boot.API.Inventario.Service.InventarioService;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Service.ProductoService;
import com.LAB1.spring.boot.API.Venta.DTO.VentaDTO;
import com.LAB1.spring.boot.API.Venta.Model.Venta;
import com.LAB1.spring.boot.API.Venta.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoService productService;
    private final FacturaService facturaService;
    private final InventarioService inventarioService;

    @Autowired
    public VentaService(VentaRepository ventaRepository, ProductoService productService, FacturaService facturaService,InventarioService inventarioService) {
        this.ventaRepository = ventaRepository;
        this.productService = productService;
        this.facturaService = facturaService;
        this.inventarioService = inventarioService;
    }

    //Obtener Entidad - lo uso para obtener entidades internas
    public Venta getVentaEntity(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    //Convertir DTO a Entidad
    private Venta convertirVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setCantidad(ventaDTO.getCantidad());
        Producto producto = productService.getProductoEntity(ventaDTO.getIdProducto());
        venta.setProducto(producto);
        Factura factura = facturaService.getFacturaEntity(ventaDTO.getIdFactura());
        venta.setFactura(factura);
        return venta;
    }

    //Convertir Entidad a DTO
    public VentaDTO convertirVentaDTO(Venta venta) {
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(venta.getId());
        ventaDTO.setCantidad(venta.getCantidad());
        ventaDTO.setIdProducto(venta.getProducto().getId());
        ventaDTO.setIdFactura(venta.getFactura().getId());
        return ventaDTO;
    }

    //Guardar Venta
    public VentaDTO saveVenta(VentaDTO ventaDTO){
        Venta venta = convertirVenta(ventaDTO);
        Venta ventaSalvada = ventaRepository.save(venta);
        return convertirVentaDTO(ventaSalvada);
    }


    //Listar Ventas
    public List<VentaDTO> getAllVentas(){
        return ventaRepository.findAll()
                .stream()
                .map(this::convertirVentaDTO)
                .collect(Collectors.toList());
    }

    //Buscar por id - lo uso para obtener un DTO que uso para el API
    public Optional<VentaDTO> getVenta(Integer id) {
        return ventaRepository.findById(id)
                .map(this::convertirVentaDTO);
    }

    //buscar por id de producto
    public List<VentaDTO> getVentaPorFactura(Integer idFactura) {
        return ventaRepository.findAllByFacturaId(idFactura)
                .stream()
                .map(this::convertirVentaDTO)
                .collect(Collectors.toList());
    }

    public VentaDTO updateVenta(Integer id, VentaDTO ventaActualizadaDTO) {
        Optional<Venta> ventaExistente = ventaRepository.findById(id);
        if(ventaExistente.isPresent()) {
            Venta venta = ventaExistente.get();
            venta.setCantidad(ventaActualizadaDTO.getCantidad());
            Venta updatedVenta = ventaRepository.save(venta);
            return convertirVentaDTO(updatedVenta);
        }
        else{
            throw new RuntimeException("Venta no encontrada");
        }
    }

    //borrar Venta
    public void deleteVenta(Integer id) {
        ventaRepository.deleteById(id);
    }

}
