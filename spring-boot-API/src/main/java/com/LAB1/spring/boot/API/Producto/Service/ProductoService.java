package com.LAB1.spring.boot.API.Producto.Service;
import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Categoria.Service.CategoriaService;
import com.LAB1.spring.boot.API.Producto.DTO.ProductoDTO;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import com.LAB1.spring.boot.API.Producto.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
    }

    //Convertir DTO a Entidad
    private Producto convertirProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());

        // Obtener la categoría completa a partir del ID
        Categoria categoria = categoriaService.getCategoriaEntity(productoDTO.getIdCategoria());
        producto.setCategoria(categoria);

        return producto;
    }

    //Convertir Entidad a DTO
    public ProductoDTO convertirProductoDTO(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setIdCategoria(producto.getCategoria().getId());
        return productoDTO;
    }

    //Guardar Producto
    public ProductoDTO saveProducto(ProductoDTO productoDTO){
        Producto producto = convertirProducto(productoDTO);
        Producto productoSalvado = productoRepository.save(producto);
        return convertirProductoDTO(productoSalvado);
    }

    //Lista Productos
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirProductoDTO)
                .collect(Collectors.toList());
    }

    //Buscar por id - lo uso para obtener un DTO que uso para el API
    public Optional<ProductoDTO> getProducto(Integer id) {
        return productoRepository.findById(id)
                .map(this::convertirProductoDTO);
    }

    //Actualizar Producto
    public ProductoDTO updateProducto(Integer id, ProductoDTO productoActualizadoDTO) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizadoDTO.getNombre());
            producto.setPrecio(productoActualizadoDTO.getPrecio());
            Categoria categoria = categoriaService.getCategoriaEntity(productoActualizadoDTO.getIdCategoria());
            producto.setCategoria(categoria);
            Producto updatedProducto = productoRepository.save(producto);
            return convertirProductoDTO(updatedProducto);

        }else{
            throw new RuntimeException("Categoria no encontrada");
        }
    }

    //borrar Producto
    public void deleteProducto(Integer id) {
        productoRepository.deleteById(id);
    }

}
