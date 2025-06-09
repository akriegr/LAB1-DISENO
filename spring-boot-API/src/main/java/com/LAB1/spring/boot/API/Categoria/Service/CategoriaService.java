package com.LAB1.spring.boot.API.Categoria.Service;

import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Categoria.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Autowired // inyeccion de dependencia del repository que maneja la conexion a la base de datos
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Convertir DTO a Entidad
    private Categoria convertirCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        return categoria;
    }

    //convertir de Entidad a DTO
    private CategoriaDTO convertirCategoriaDTO(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getNombre());
        categoriaDTO.setDescripcion(categoria.getDescripcion());
        return categoriaDTO;
    }

    //Guardar Cateogira
    public CategoriaDTO saveCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertirCategoria(categoriaDTO);
        Categoria categoriaSalvada = categoriaRepository.save(categoria);
        return convertirCategoriaDTO(categoriaSalvada);
    }

    //Lista Categorias
    public List<CategoriaDTO> getAllCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirCategoriaDTO)
                .collect(Collectors.toList());
    }

    //Buscar por id
    public Optional<CategoriaDTO> getCategoria(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::convertirCategoriaDTO);
    }

    //Actualizar Categoria
    public CategoriaDTO updateCategoria(Integer id, CategoriaDTO categoriaActualizadaDto) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isPresent()) {
            Categoria categoria = categoriaExistente.get();
            categoria.setNombre(categoriaActualizadaDto.getNombre());
            categoria.setDescripcion(categoriaActualizadaDto.getDescripcion());
            Categoria updatedCategoria = categoriaRepository.save(categoria);
            return convertirCategoriaDTO(updatedCategoria);

        }else{
            throw new RuntimeException("Categoria no encontrada");
        }
    }

    //borrar categoria
    public void deleteCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

}
