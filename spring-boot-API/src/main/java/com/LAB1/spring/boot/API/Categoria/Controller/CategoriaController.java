package com.LAB1.spring.boot.API.Categoria.Controller;

import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import com.LAB1.spring.boot.API.Categoria.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/LAB")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired //inyecto dependencia de
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    //Crear nueva categoria

    @PostMapping("/categoria")
    public ResponseEntity<?> addCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try{
            CategoriaDTO newCategoriaDTO = categoriaService.saveCategoria(categoriaDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Categoria creado con exito");
        }catch(Exception e){
            System.err.println("Error al crear la categoria: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al crear categoria");
        }
    }

    //Listar todas las categorias
    @GetMapping("/categoria")
    public ResponseEntity<?> getAllCategoria() {
        try{
            List<CategoriaDTO> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(categorias);
        }catch(Exception e){
            System.err.println("Error al obtener las categorias: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener categorias");
        }
    }

    //Obtener categoria especifica
    @GetMapping("/categoria/{id}")
    public ResponseEntity<?> getCategoriaById(@PathVariable int id) {
        try{
            Optional<CategoriaDTO> categoria = categoriaService.getCategoria(id);
            return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch(Exception e){
            System.err.println("Error al obtener la categoria: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al obtener la categoria");
        }
    }

    @PutMapping("/categoria/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable int id,@RequestBody CategoriaDTO categoriaDTO) {
        try{
            CategoriaDTO updatedCategoria = categoriaService.updateCategoria(id, categoriaDTO);
            return ResponseEntity.ok(updatedCategoria);
        }catch(Exception e){
            System.err.println("Error al actualizar la categoria: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la categoria");
        }
    }

    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable int id) {
        try{
            categoriaService.deleteCategoria(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Categoria eliminada con exito");
        }catch(Exception e){
            System.err.println("Error al eliminar la categoria: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoria");
        }
    }

}
