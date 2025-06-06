package com.LAB1.spring.boot.API.Categoria.Service;

import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Categoria.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Autowired // inyeccion de dependencia del repository que maneja la conexion a la base de datos
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Guardar Cateogira
    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    //Lista Categorias
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    //Buscar por id
    public Optional<Categoria> getCategoria(Integer id) {
        return categoriaRepository.findById(id);
    }

    //Actualizar Categoria
    public Categoria updateCategoria(Integer id, Categoria categoriaActualizada) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isPresent()) {
            Categoria categoria = categoriaExistente.get();
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            return categoriaRepository.save(categoria);
        }else{
            throw new RuntimeException("Categoria no encontrada");
        }
    }

    //borrar categoria
    public void deleteCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

}
