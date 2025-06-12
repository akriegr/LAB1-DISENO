package com.LAB1.spring.boot.API.Inventario.Repository;

import com.LAB1.spring.boot.API.Inventario.Model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    //Jpa ya tiene como buscar por ciertos id por ende lo hace solo
    Optional<Inventario> findByProductoId(Integer productoId);
}
