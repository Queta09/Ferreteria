package org.example.f.servicios;

import org.example.f.modelos.Descuento;
import org.example.f.modelos.Descuento.TipoDescuento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio POO encargada de gestionar el catálogo de descuentos disponibles.
 * <p>
 * Implementa {@link java.io.Serializable} para permitir la persistencia de su estado
 * (aunque actualmente los datos son inicializados en memoria).
 * </p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class DescuentoManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Lista interna que almacena todos los objetos Descuento disponibles en el sistema. */
    private List<Descuento> catalogoDescuentos;

    /**
     * Constructor de la clase. Inicializa la lista de descuentos y carga
     * un conjunto de descuentos de prueba.
     */
    public DescuentoManager() {
        this.catalogoDescuentos = new ArrayList<>();
        inicializarDescuentos();
    }

    /**
     * Carga un conjunto de descuentos fijos de prueba en el catálogo interno.
     */
    private void inicializarDescuentos() {
        catalogoDescuentos.add(new Descuento("TOT10", 0.10, TipoDescuento.PORCENTAJE, "10% en el total de la venta"));
        catalogoDescuentos.add(new Descuento("FIX50", 50.00, TipoDescuento.MONTO_FIJO, "Descuento fijo de $50.00"));
        catalogoDescuentos.add(new Descuento("PROMO25", 0.25, TipoDescuento.PORCENTAJE, "Promoción de temporada: 25%"));
    }

    /**
     * Obtiene una copia de la lista completa de todos los descuentos disponibles.
     * @return Una nueva {@code ArrayList} que contiene todos los objetos Descuento.
     */
    public List<Descuento> obtenerTodosLosDescuentos() {
        return new ArrayList<>(this.catalogoDescuentos);
    }

    /**
     * Busca un descuento en el catálogo utilizando su código único.
     * La búsqueda se realiza sin importar mayúsculas/minúsculas o espacios iniciales/finales.
     * * @param codigo El código del descuento a buscar.
     * @return Un {@code Optional} que contiene el objeto Descuento si se encuentra una coincidencia.
     */
    public Optional<Descuento> buscarDescuentoPorCodigo(String codigo) {
        String codigoNormalizado = codigo.trim().toUpperCase();
        return this.catalogoDescuentos.stream()
                .filter(d -> d.getCodigo().equals(codigoNormalizado))
                .findFirst();
    }
}