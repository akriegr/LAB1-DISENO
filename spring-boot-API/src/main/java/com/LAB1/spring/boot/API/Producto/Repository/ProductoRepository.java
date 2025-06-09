package com.LAB1.spring.boot.API.Producto.Repository;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
