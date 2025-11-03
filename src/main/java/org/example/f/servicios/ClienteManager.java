package org.example.f.servicios;

import org.example.f.modelos.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio POO encargada de la gestión del catálogo de Clientes.
 * Implementa las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y maneja
 * la persistencia de datos mediante la serialización a un archivo binario.
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 * @since 2025-11-03
 */
public class ClienteManager {

    /** Lista interna (simulación de base de datos) que contiene todos los objetos Cliente. */
    private final List<Cliente> catalogoClientes;
    /** Contador para asignar el próximo ID único a un nuevo cliente. */
    private int nextId = 1;
    /** Nombre del archivo binario utilizado para la serialización de datos. */
    private static final String FILE_NAME = "clientes.dat";

    /**
     * Constructor de la clase. Inicializa la lista interna y maneja la carga
     * de datos desde el archivo de persistencia. Si el archivo no existe,
     * carga datos iniciales de prueba.
     */
    public ClienteManager() {
        this.catalogoClientes = new ArrayList<>();

        if (!cargarDatos()) {
            cargarDatosIniciales();
            guardarDatos();
        }
    }

    /**
     * Carga un conjunto de clientes iniciales si no se encuentra un archivo de persistencia.
     */
    private void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales de prueba...");

        Cliente c1 = new Cliente();
        c1.setNombre("Juan Pérez");
        c1.setTelefono("5512345678");
        c1.setEmail("juan.perez@mail.com");
        c1.setDireccion("Calle 269");
        registrarClienteInterno(c1);

        Cliente c2 = new Cliente();
        c2.setNombre("Ana Gómez");
        c2.setTelefono("5598765432");
        c2.setEmail("ana.gomez@mail.com");
        c2.setDireccion("Avenida 345");
        registrarClienteInterno(c2);
    }

    /**
     * Método interno para añadir un nuevo cliente a la lista, asignándole el siguiente ID.
     * @param cliente El objeto Cliente a registrar.
     */
    private void registrarClienteInterno(Cliente cliente) {
        cliente.setIdCliente(nextId++);
        this.catalogoClientes.add(cliente);
    }

    /**
     * Registra un nuevo cliente en el catálogo (Operación CRUD: Create).
     * Asigna un nuevo ID, añade el cliente a la lista interna y persiste el catálogo.
     * @param cliente El objeto Cliente nuevo a guardar.
     */
    public void guardarCliente(Cliente cliente) {
        cliente.setIdCliente(nextId++);
        this.catalogoClientes.add(cliente);
        System.out.println("Cliente CREADO y registrado: " + cliente.getNombre() + " (ID: " + cliente.getIdCliente() + ")");

        guardarDatos();
    }

    /**
     * Actualiza los datos de un cliente existente en el catálogo (Operación CRUD: Update).
     * Busca el cliente por ID, reemplaza el objeto en la lista interna y persiste el catálogo.
     * @param clienteActualizado El objeto Cliente con los datos modificados.
     */
    public void actualizarCliente(Cliente clienteActualizado) {
        int index = findClienteIndexById(clienteActualizado.getIdCliente());

        if (index != -1) {
            this.catalogoClientes.set(index, clienteActualizado);
            System.out.println("Cliente ACTUALIZADO: " + clienteActualizado.getNombre() + " (ID: " + clienteActualizado.getIdCliente() + ")");

            guardarDatos();
        } else {
            System.out.println("Error: No se puede actualizar. Cliente ID " + clienteActualizado.getIdCliente() + " no encontrado.");
        }
    }

    /**
     * Elimina un cliente del catálogo basado en su ID (Operación CRUD: Delete).
     * @param idCliente El ID del cliente a eliminar.
     */
    public void eliminarCliente(int idCliente) {
        boolean eliminado = this.catalogoClientes.removeIf(c -> c.getIdCliente() == idCliente);

        if (eliminado) {
            System.out.println("Cliente ID " + idCliente + " eliminado.");
            guardarDatos();
        } else {
            System.out.println("Cliente ID " + idCliente + " no encontrado para eliminar.");
        }
    }

    /**
     * Escribe el catálogo completo de clientes (la lista interna) y el contador de ID
     * al archivo de persistencia mediante serialización.
     */
    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this.catalogoClientes);
            oos.writeInt(this.nextId);
            System.out.println("✅ Clientes guardados en " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar clientes: " + e.getMessage());
        }
    }

    /**
     * Carga el catálogo de clientes desde el archivo de persistencia.
     * @return {@code true} si la carga fue exitosa, {@code false} si el archivo no existe o hay un error.
     */
    private boolean cargarDatos() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            @SuppressWarnings("unchecked")
            List<Cliente> loadedList = (List<Cliente>) ois.readObject();
            this.catalogoClientes.addAll(loadedList);
            this.nextId = ois.readInt();
            System.out.println("✅ Clientes cargados exitosamente desde " + FILE_NAME);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error al cargar clientes. Se usará nueva lista. " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una copia de la lista completa de todos los clientes en el catálogo (Operación CRUD: Read).
     * @return Una nueva {@code ArrayList} que contiene todos los objetos Cliente.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(catalogoClientes);
    }

    /**
     * Busca el índice de un cliente dentro de la lista interna basado en su ID.
     * @param idCliente El ID del cliente a buscar.
     * @return El índice del cliente en la lista, o -1 si no se encuentra.
     */
    private int findClienteIndexById(int idCliente) {
        for (int i = 0; i < catalogoClientes.size(); i++) {
            if (catalogoClientes.get(i).getIdCliente() == idCliente) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca un cliente por ID (numérico) o por coincidencia parcial de nombre (texto).
     * @param busqueda Cadena de texto a buscar (puede ser un ID o parte del nombre).
     * @return Un {@code Optional} que contiene el objeto Cliente si se encuentra una coincidencia.
     */
    public Optional<Cliente> buscarCliente(String busqueda) {
        try {
            // Intenta buscar por ID
            int id = Integer.parseInt(busqueda.trim());
            return catalogoClientes.stream().filter(c -> c.getIdCliente() == id).findFirst();
        } catch (NumberFormatException e) {
            // Si falla, busca por nombre (coincidencia parcial sin importar mayúsculas/minúsculas)
            String nombreBusqueda = busqueda.trim().toLowerCase();
            return catalogoClientes.stream().filter(c -> c.getNombre().toLowerCase().contains(nombreBusqueda)).findFirst();
        }
    }
}