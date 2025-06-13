package com.LAB1.spring.boot.API.Venta.Repository;

import com.LAB1.spring.boot.API.Venta.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findAllByFacturaId(Integer facturaId);
}
