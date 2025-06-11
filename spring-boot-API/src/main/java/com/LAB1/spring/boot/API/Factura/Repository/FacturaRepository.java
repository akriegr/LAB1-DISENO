package com.LAB1.spring.boot.API.Factura.Repository;

import com.LAB1.spring.boot.API.Factura.Model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
}
