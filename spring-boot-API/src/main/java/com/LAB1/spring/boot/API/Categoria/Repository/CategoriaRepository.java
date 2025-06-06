package com.LAB1.spring.boot.API.Categoria.Repository;
import com.LAB1.spring.boot.API.Categoria.Model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
