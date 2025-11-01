// Archivo: org.example.f.service/ClienteManager.java

package org.example.f.servicios;

import org.example.f.modelos.Cliente; // Asegúrate de usar el paquete correcto
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Clase de servicio POO para gestionar clientes
public class ClienteManager {

    private final List<Cliente> catalogoClientes; // Simulación de base de datos
    private int nextId = 1;

    public ClienteManager() {
        this.catalogoClientes = new ArrayList<>();
        // Cargar algunos clientes iniciales para pruebas
        registrarCliente(new Cliente(nextId++, "Juan Pérez","269","5512345678", "juan.perez@mail.com"));
        registrarCliente(new Cliente(nextId++, "Ana Gómez","345","5598765432", "ana.gomez@mail.com"));
    }

    /**
     * Registra un nuevo cliente en el sistema.
     */
    public void registrarCliente(Cliente cliente) {
        // En una implementación real, aquí se guardaría en la base de datos
        if (cliente.getIdCliente() == 0) {
            cliente.setIdCliente(nextId++); // Asignar ID si es nuevo
        }
        this.catalogoClientes.add(cliente);
        System.out.println("Cliente registrado: " + cliente.getNombre());
    }

    /**
     * Busca un cliente por ID o por nombre.
     * @param busqueda Cadena de búsqueda (ID o parte del nombre).
     * @return Un Optional que contiene el Cliente si se encuentra.
     */
    public Optional<Cliente> buscarCliente(String busqueda) {
        try {
            // Intentar buscar por ID (si es un número)
            int id = Integer.parseInt(busqueda.trim());
            return catalogoClientes.stream()
                    .filter(c -> c.getIdCliente() == id)
                    .findFirst();
        } catch (NumberFormatException e) {
            // Buscar por nombre si no es un ID válido
            String nombreBusqueda = busqueda.trim().toLowerCase();
            return catalogoClientes.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(nombreBusqueda))
                    .findFirst();
        }
    }

    /**
     * Obtiene la lista completa de clientes.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(catalogoClientes);
    }

    // Aquí irían métodos para editarCliente, eliminarCliente, etc.
}