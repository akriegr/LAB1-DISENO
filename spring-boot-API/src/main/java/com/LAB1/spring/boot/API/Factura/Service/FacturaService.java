package com.LAB1.spring.boot.API.Factura.Service;

import com.LAB1.spring.boot.API.Categoria.DTO.CategoriaDTO;
import com.LAB1.spring.boot.API.Factura.DTO.FacturaDTO;
import com.LAB1.spring.boot.API.Factura.Model.Factura;
import com.LAB1.spring.boot.API.Factura.Repository.FacturaRepository;
import com.LAB1.spring.boot.API.Producto.Model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    private FacturaRepository facturaRepository;

    @Autowired
    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    //Obtener Entidad - lo uso para obtener entidades internas
    public Factura getFacturaEntity(Integer id) {
        return facturaRepository.findById(id).orElse(null);
    }

    //Convertir DTO a Entidad
    private Factura convertirFactura(FacturaDTO facturaDTO) {
        Factura factura = new Factura();
        factura.setMonto(facturaDTO.getMonto());
        factura.setFecha(facturaDTO.getFecha());
        return factura;
    }

    //Convertir Entidad a DTO
    private FacturaDTO convertirFacturaDTO(Factura factura) {
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setId(factura.getId());
        facturaDTO.setMonto(factura.getMonto());
        facturaDTO.setFecha(factura.getFecha());
        return facturaDTO;
    }

    //Guardar Factura
    public FacturaDTO saveFactura(FacturaDTO facturaDTO) {
        Factura factura = convertirFactura(facturaDTO);
        Factura facturaGuardada = facturaRepository.save(factura);
        return convertirFacturaDTO(facturaGuardada);
    }

    //Listar Facturas
    public List<FacturaDTO> getAllFacturas() {
        return facturaRepository.findAll()
                .stream()
                .map(this::convertirFacturaDTO)
                .collect(Collectors.toList());
    }

    //Buscar por id - lo uso para obtener un DTO que uso para el api
    public Optional<FacturaDTO> getFactura(Integer id) {
        return facturaRepository.findById(id)
                .map(this::convertirFacturaDTO);
    }

    //Actualizar Factura
    public FacturaDTO updateFactura(Integer id,FacturaDTO facturaDTOActualizado) {
        Optional<Factura> facturaExistente = facturaRepository.findById(id);
        if (facturaExistente.isPresent()) {
            Factura factura = facturaExistente.get();
            factura.setMonto(facturaDTOActualizado.getMonto());
            factura.setFecha(facturaDTOActualizado.getFecha());
            Factura updatedFactura = facturaRepository.save(factura);
            return convertirFacturaDTO(updatedFactura);
        }else{
            throw new RuntimeException("Categoria no encontrada");
        }
    }

    //Borrar factura
    public void deleteFactura(Integer id) {
        facturaRepository.deleteById(id);
    }

}
