package com.LAB1.spring.boot.API.Inventario.Service;

import com.LAB1.spring.boot.API.Inventario.DTO.InventarioDTO;
import com.LAB1.spring.boot.API.Inventario.Model.Inventario;
import com.LAB1.spring.boot.API.Inventario.Repository.InventarioRepository;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ProductoService productoService;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository, ProductoService productoService) {
        this.inventarioRepository = inventarioRepository;
        this.productoService = productoService;
    }

    //Convertir DTO a Entidad
    private Inventario convertirInventario (InventarioDTO inventarioDTO) {
        Inventario inventario = new Inventario();
        inventario.setCantidad(inventarioDTO.getCantidad());
        //Agarro el producto completo a partir del id
        Producto producto = productoService.getProductoEntity(inventarioDTO.getIdProducto());
        inventario.setProducto(producto);
        return inventario;
    }

    //Convertir Entidad a DTO
    public InventarioDTO convertirInventarioDTO(Inventario inventario) {
        InventarioDTO inventarioDTO = new InventarioDTO();
        inventarioDTO.setId(inventario.getId());
        inventarioDTO.setCantidad(inventario.getCantidad());
        inventarioDTO.setIdProducto(inventario.getProducto().getId());
        return inventarioDTO;
    }

    //Guardar Inventario
    public InventarioDTO saveInventario(InventarioDTO inventarioDTO) {
        Inventario inventario = convertirInventario(inventarioDTO);
        Inventario inventarioSalvado = inventarioRepository.save(inventario);
        return convertirInventarioDTO(inventarioSalvado);
    }

    //Listar Inventario
    public List<InventarioDTO>getAllInventario(){
        return inventarioRepository.findAll()
                .stream()
                .map(this::convertirInventarioDTO)
                .collect(Collectors.toList());
    }

    //Buscar inventario por id producto
    public Optional<InventarioDTO> getInventarioByIdProducto(Integer idProducto) {
        return inventarioRepository.findByProductoId(idProducto)
                .map(this::convertirInventarioDTO);
    }

    //Actualizar cantidad de inventario
    public InventarioDTO updateInventario(Integer id,InventarioDTO inventarioDTOactualizado) {
        Optional<Inventario> inventarioExistente = inventarioRepository.findById(id);
        if (inventarioExistente.isPresent()) {
            Inventario inventario = inventarioExistente.get();
            inventario.setCantidad(inventarioDTOactualizado.getCantidad());
            Inventario updatedInventario = inventarioRepository.save(inventario);
            return  convertirInventarioDTO(updatedInventario);
        }else{
            throw new RuntimeException("Categoria no encontrada");
        }
    }

    //Borrar Inventario
    public void deleteInventario(Integer id) {
        inventarioRepository.deleteById(id);
    }

}
