// Archivo: org.example.f.servicios/DescuentoManager.java

package org.example.f.servicios;

import org.example.f.modelos.Descuento;
import org.example.f.modelos.Descuento.TipoDescuento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DescuentoManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Descuento> catalogoDescuentos;

    public DescuentoManager() {
        this.catalogoDescuentos = new ArrayList<>();
        // Inicializar con descuentos de prueba para que el ChoiceBox se llene.
        inicializarDescuentos();
    }

    private void inicializarDescuentos() {
        // Descuento del 10%
        catalogoDescuentos.add(new Descuento("TOT10", 0.10, TipoDescuento.PORCENTAJE, "10% en el total de la venta"));
        // Descuento de monto fijo
        catalogoDescuentos.add(new Descuento("FIX50", 50.00, TipoDescuento.MONTO_FIJO, "Descuento fijo de $50.00"));
        // Descuento promocional
        catalogoDescuentos.add(new Descuento("PROMO25", 0.25, TipoDescuento.PORCENTAJE, "Promoción de temporada: 25%"));
    }

    public List<Descuento> obtenerTodosLosDescuentos() {
        return new ArrayList<>(this.catalogoDescuentos);
    }

    // Método necesario para la lógica de búsqueda, aunque no se usa en este formulario
    public Optional<Descuento> buscarDescuentoPorCodigo(String codigo) {
        String codigoNormalizado = codigo.trim().toUpperCase();
        return this.catalogoDescuentos.stream()
                .filter(d -> d.getCodigo().equals(codigoNormalizado))
                .findFirst();
    }
}